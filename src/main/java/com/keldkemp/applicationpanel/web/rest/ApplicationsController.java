package com.keldkemp.applicationpanel.web.rest;

import com.keldkemp.applicationpanel.github.GitHubService;
import com.keldkemp.applicationpanel.github.domain.GitHubUserRepo;
import com.keldkemp.applicationpanel.github.domain.GitHubUserRepos;
import com.keldkemp.applicationpanel.github.domain.GitHubWorkflowsRepo;
import com.keldkemp.applicationpanel.service.ApplicationsService;
import com.keldkemp.applicationpanel.web.rest.dto.ApplicationDto;
import com.keldkemp.applicationpanel.web.rest.dto.DockerDto;
import com.keldkemp.applicationpanel.web.rest.dto.FileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/applications")
public class ApplicationsController {

    @Autowired
    private GitHubService gitHubService;

    @Autowired
    private ApplicationsService applicationsService;

    @GetMapping()
    public List<ApplicationDto> getMyApplications() {
        return applicationsService.getAllMyApplications();
    }

    @GetMapping("/all")
    public GitHubUserRepos getAllApplicationInGithub() {
        return gitHubService.getAllRepos();
    }

    @GetMapping("/{owner}/{repo}")
    public GitHubUserRepo getUserRepo(@PathVariable String owner, @PathVariable String repo) {
        return gitHubService.getUserRepo(owner + "/" + repo);
    }

    @PostMapping("/workflow")
    public GitHubWorkflowsRepo getAllWorkflow(@RequestBody GitHubUserRepo gitHubUserRepo) {
        return gitHubService.getAllWorkflows(gitHubUserRepo);
    }

    @GetMapping("/{id}")
    public ApplicationDto getApplications(@PathVariable Long id) {
        return applicationsService.getApplicationDtoById(id);
    }

    @GetMapping("/find")
    public ApplicationDto getApplications(@RequestParam(name = "name") String name) {
        return applicationsService.getApplicationDtoByOriginalName(name);
    }

    @PostMapping("/add")
    public ApplicationDto addApplication(@RequestBody ApplicationDto applicationDto) {
        return applicationsService.addApplication(applicationDto);
    }

    @PostMapping("/edit")
    public ApplicationDto editApplication(@RequestBody ApplicationDto applicationDto) {
        return applicationsService.editApplication(applicationDto);
    }

    @PostMapping("/deploy")
    public DockerDto deployApplication(@RequestBody ApplicationDto applicationDto) {
        return applicationsService.deployApp(applicationDto);
    }

    @PostMapping("/update")
    public DockerDto updateApplication(@RequestBody ApplicationDto applicationDto) {
        return applicationsService.updateApp(applicationsService.getApplicationById(applicationDto.getId()));
    }

    @PostMapping("/cancelUpdate")
    public void cancelUpdate(@RequestBody ApplicationDto applicationDto) {
        applicationsService.cancelUpdate(applicationDto);
    }

    @GetMapping("/{id}/files")
    public List<FileDto> getAllFiles(@PathVariable Long id) {
        return applicationsService.getAllFiles(id);
    }

    @GetMapping("/{id}/file")
    public FileDto getFileBody(@PathVariable Long id, @RequestParam(name = "name") String fileName) {
        return applicationsService.getFileBody(id, fileName);
    }

    @PostMapping("/{id}/file")
    public FileDto writeFile(@RequestBody FileDto fileDto, @PathVariable Long id) {
        return applicationsService.writeFile(fileDto);
    }
}
