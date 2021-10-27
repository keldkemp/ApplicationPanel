package com.keldkemp.applicationpanel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keldkemp.applicationpanel.models.Applications;
import com.keldkemp.applicationpanel.util.StringUtil;
import com.keldkemp.applicationpanel.web.rest.dto.DataBaseDto;
import com.keldkemp.applicationpanel.web.rest.dto.DockerDto;
import com.keldkemp.applicationpanel.web.rest.dto.DockerInspectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class DockerServiceImpl implements DockerService {

    private static final String CONTAINER_NAME = "container_";

    private static final String DOCKER_COMPOSE = "docker-compose.yml";

    private static final String VERSION = "3.8";

    @Autowired
    private ProjectsFolderService projectsFolderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SettingsService settingsService;

    @Override
    public DockerDto createDockerCompose(Applications application) {
        String lines = dockerCompose(application);

        projectsFolderService.writeToFile(application, DOCKER_COMPOSE, lines);

        return getDockerDto(application, "Docker-compose created!");
    }

    @Override
    public DockerDto createDockerCompose(Applications application, DataBaseDto dataBaseDto) {
        String lines = dockerCompose(application);

        lines += getEnvironment(dataBaseDto);

        projectsFolderService.writeToFile(application, DOCKER_COMPOSE, lines);

        return getDockerDto(application, "Docker-compose created!");
    }

    @Override
    public DockerInspectDto inspect(Applications application) {
        String result = execute(String.format("docker inspect %s", CONTAINER_NAME + StringUtil.toLowerCase(application.getName())));
        result = result.replaceFirst("^\\[", "");
        result = result.replaceAll("]$", "");
        try {
            return objectMapper.readValue(result, DockerInspectDto.class);
        } catch (Exception e) {
            //TODO
            return null;
        }
    }

    @Override
    public DockerDto startDockerCompose(Applications application) {
        String path = StringUtil.getPath(settingsService.getProjectsFolderPath(), application.getOriginalName()) + DOCKER_COMPOSE;
        String result = execute(String.format("docker-compose -f %s up --detach", path));
        DockerDto dockerDto = getDockerDto(application, result);
        DockerInspectDto inspectDto = inspect(application);
        if (inspectDto != null && inspectDto.getState().getStatus().equals("running")) {
            dockerDto.setStatus(true);
        }
        return dockerDto;
    }

    @Override
    public DockerDto pull(Applications application) {
        String result = execute(String.format("docker pull %s", StringUtil.toLowerCase(application.getOriginalName())));
        DockerDto dockerDto = getDockerDto(application, result);
        if (result.contains("Status: Downloaded newer image for") || result.contains("Status: Image is up to date for")) {
            dockerDto.setStatus(true);
        }
        return dockerDto;
    }

    @Override
    public DockerDto start(Applications application) {
        String result = execute(String.format("docker start %s", CONTAINER_NAME + StringUtil.toLowerCase(application.getName())));
        DockerDto dockerDto = getDockerDto(application, result);
        if (result.contains(CONTAINER_NAME + StringUtil.toLowerCase(application.getName()))) {
            dockerDto.setStatus(true);
        }
        return dockerDto;
    }

    @Override
    public DockerDto stop(Applications application) {
        String result = execute(String.format("docker stop %s", CONTAINER_NAME + StringUtil.toLowerCase(application.getName())));
        DockerDto dockerDto = getDockerDto(application, result);
        if (result.contains(CONTAINER_NAME + StringUtil.toLowerCase(application.getName()))) {
            dockerDto.setStatus(true);
        }
        return dockerDto;
    }

    @Override
    public DockerDto removeContainer(Applications application) {
        String result = execute(String.format("docker container rm %s", CONTAINER_NAME + StringUtil.toLowerCase(application.getName())));
        DockerDto dockerDto = getDockerDto(application, result);
        if (result.contains(CONTAINER_NAME + StringUtil.toLowerCase(application.getName()))) {
            dockerDto.setStatus(true);
        }
        return dockerDto;
    }

    @Override
    public DockerDto removeImage(Applications application) {
        String result = execute(String.format("docker image rm %s", StringUtil.toLowerCase(application.getOriginalName())));
        DockerDto dockerDto = getDockerDto(application, result);
        if (!result.contains("Error")) {
            dockerDto.setStatus(true);
        }
        return dockerDto;
    }

    private String dockerCompose(Applications application) {
        String lines = String.format("version: \"%s\"\n\n", VERSION);

        lines += "services:\n  application:\n    ";
        lines += String.format("image: %s\n    ", StringUtil.toLowerCase(application.getOriginalName()));
        lines += "restart: always\n    ";
        lines += String.format("container_name: %s\n    ", CONTAINER_NAME + StringUtil.toLowerCase(application.getName()));
        lines += "network_mode: \"host\"\n    ";

        return lines;
    }

    private DockerDto getDockerDto(Applications application, String msg) {
        DockerDto dto = new DockerDto();
        dto.setApplicationId(application.getId());
        dto.setApplicationName(application.getName());
        dto.setApplicationOriginalName(application.getOriginalName());
        dto.setResult(msg);

        return dto;
    }

    private String getEnvironment(DataBaseDto dto) {
        String env = "environment:\n";
        env += String.format("      - DATABASE_URL=%s", dto.getUrl());
        return env;
    }

    private String execute(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            return readProcessAndClose(process);
        } catch (Exception e) {
            //TODO
            throw new NullPointerException();
        }
    }

    private String readProcessAndClose(Process process) {
        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String data = br.lines().collect(Collectors.joining("\n"));
        try {
            process.waitFor();
        } catch (Exception e) {
            //TODO
        } finally {
            process.destroy();
        }
        return data;
    }
}
