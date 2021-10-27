package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.errors.GitHubActionsException;
import com.keldkemp.applicationpanel.github.GitHubService;
import com.keldkemp.applicationpanel.github.domain.GitHubWorkflow;
import com.keldkemp.applicationpanel.github.domain.GitHubWorkflowsRepo;
import com.keldkemp.applicationpanel.models.Applications;
import com.keldkemp.applicationpanel.repositories.ApplicationsRepository;
import com.keldkemp.applicationpanel.web.rest.dto.ApplicationDto;
import com.keldkemp.applicationpanel.web.rest.dto.DataBaseDto;
import com.keldkemp.applicationpanel.web.rest.dto.DockerDto;
import com.keldkemp.applicationpanel.web.rest.dto.FileDto;
import com.keldkemp.applicationpanel.web.rest.mappers.ApplicationsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ApplicationsServiceImpl implements ApplicationsService {

    @Autowired
    private ApplicationsRepository applicationsRepository;

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private ApplicationsMapper applicationsMapper;

    @Autowired
    private DataBaseService dataBaseService;

    @Autowired
    private DockerService dockerService;

    @Autowired
    private ApplicationSchedulerService applicationSchedulerService;

    @Autowired
    private ProjectsFolderService projectsFolderService;

    @Override
    public ApplicationDto getApplicationDtoByOriginalName(String name) {
        Applications app = applicationsRepository.findByOriginalName(name);
        if (app == null) {
            ApplicationDto dto = new ApplicationDto();
            return dto;
        } else {
            return applicationsMapper.applicationDto(app);
        }
    }

    @Override
    public ApplicationDto getApplicationDtoById(Long id) {
        return applicationsMapper.applicationDto(applicationsRepository.findById(id).get());
    }

    @Override
    public Applications getApplicationById(Long id) {
        return applicationsRepository.findById(id).get();
    }

    @Override
    public List<ApplicationDto> getAllMyApplications() {
        return applicationsMapper.applicationsDto(StreamSupport
                .stream(applicationsRepository.findAll().spliterator(), false).collect(Collectors.toList()));
    }

    @Override
    public ApplicationDto addApplication(ApplicationDto applicationDto) {
        validateApplication(applicationDto);

        Applications applications = applicationsMapper.toApplications(applicationDto);
        applicationsRepository.save(applications);

        return applicationsMapper.applicationDto(applications);
    }

    @Override
    public ApplicationDto editApplication(ApplicationDto applicationDto) {
        Applications applications = applicationsMapper.toApplications(applicationDto);

        applicationsRepository.save(applications);
        setAutoUpdate(applications);

        return applicationsMapper.applicationDto(applications);
    }

    @Override
    public DockerDto deployApp(ApplicationDto applicationDto, DataBaseDto dataBase) {
        Applications application = getApplicationById(applicationDto.getId());
        dockerService.createDockerCompose(application);
        dockerService.pull(application);
        DockerDto dockerCompose = dockerService.startDockerCompose(application);

        if (dockerCompose.getStatus()) {
            application.setStatusApplications(1);
            application.setLastUpdate(new Date());

            applicationsRepository.save(application);

            setAutoUpdate(application);
        }

        return dockerCompose;
    }

    @Override
    public DockerDto deployApp(ApplicationDto applicationDto) {
        Applications application = getApplicationById(applicationDto.getId());

        if (applicationDto.getOptions().getIsCreateDb()) {
            DataBaseDto db = dataBaseService.generateDbAndOwner(application);
            dockerService.createDockerCompose(application, db);
        } else {
            dockerService.createDockerCompose(application);
        }

        dockerService.pull(application);

        if (applicationDto.getOptions().getIsStartApp()) {
            DockerDto dockerCompose = dockerService.startDockerCompose(application);

            if (dockerCompose.getStatus() != null && dockerCompose.getStatus()) {
                application.setStatusApplications(1);
                application.setLastUpdate(new Date());

                applicationsRepository.save(application);

                setAutoUpdate(application);
            }

            return dockerCompose;
        } else {
            return null;
        }
    }

    @Override
    public DockerDto updateApp(Applications application) {
        DockerDto dockerStop = dockerService.stop(application);
        DockerDto dockerRemoveContainer = dockerService.removeContainer(application);
        DockerDto dockerRemoveImage = dockerService.removeImage(application);

        DockerDto dockerPull = dockerService.pull(application);
        DockerDto dockerCompose = dockerService.startDockerCompose(application);

        if (dockerCompose.getStatus() != null && dockerCompose.getStatus()) {
            application.setLastUpdate(new Date());
            application.setStatusApplications(1);
        } else {
            application.setStatusApplications(0);
        }
        applicationsRepository.save(application);

        return dockerCompose;
    }

    @Override
    public void cancelUpdate(ApplicationDto applicationDto) {
        Applications application = getApplicationById(applicationDto.getId());
        applicationSchedulerService.cancelUpdate(application);

        application.setIsAutoUpdate(0);
        applicationsRepository.save(application);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void autoUpdateApp() {
        applicationsRepository.findAllByIsUpdate().forEach(app -> applicationSchedulerService.handleApplicationUpdate(app));
    }

    @Override
    public List<FileDto> getAllFiles(Long id) {
        Applications applications = getApplicationById(id);

        return projectsFolderService.getAllFilesInApp(applications);
    }

    @Override
    public FileDto getFileBody(Long id, String fileName) {
        Applications applications = getApplicationById(id);

        return projectsFolderService.readFile(applications, fileName);
    }

    @Override
    public FileDto writeFile(FileDto file) {
        Applications applications = getApplicationById(file.getApplicationId());

        projectsFolderService.writeToFile(applications, file.getFileName(), file.getText());

        return file;
    }

    private void setAutoUpdate(Applications application) {
        if (application.getStatusApplications() != null && application.getStatusApplications() == 1
                && application.getIsAutoUpdate() == 1 && application.getUpdateDateCronFormat() != null) {
            applicationSchedulerService.handleApplicationUpdate(application);
        } else {
            applicationSchedulerService.cancelUpdate(application);
        }
    }

    private void validateApplication(ApplicationDto applicationDto) {
        GitHubWorkflowsRepo gitHubWorkflowsRepo = gitHubService.getAllWorkflows(applicationDto.getGitHubUserRepo());

        if (gitHubWorkflowsRepo.getTotalCount() < 1) {
            throw  new GitHubActionsException("Docker actions", "url", applicationDto.getGitHubUrl(),
                    "name", applicationDto.getOriginalName());
        }

        List<GitHubWorkflow> list = gitHubWorkflowsRepo.getWorkflows().stream()
                .filter(gitHubWorkflow -> gitHubWorkflow.getName().toLowerCase().contains("push docker image")
                    || gitHubWorkflow.getPath().toLowerCase().contains("push_docker_image.yml")
                        || gitHubWorkflow.getPath().toLowerCase().contains("docker"))
                .collect(Collectors.toList());

        if (list.size() < 1) {
            throw  new GitHubActionsException("Docker actions", "url", applicationDto.getGitHubUrl(),
                    "name", applicationDto.getOriginalName());
        }
    }
}
