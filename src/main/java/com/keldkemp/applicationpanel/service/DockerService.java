package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.Applications;
import com.keldkemp.applicationpanel.web.rest.dto.DataBaseDto;
import com.keldkemp.applicationpanel.web.rest.dto.DockerDto;
import com.keldkemp.applicationpanel.web.rest.dto.DockerInspectDto;

public interface DockerService {
    DockerDto pull(Applications application);

    DockerDto stop(Applications application);

    DockerDto start(Applications application);

    DockerDto removeContainer(Applications application);

    DockerDto removeImage(Applications application);

    DockerInspectDto inspect(Applications application);

    DockerDto createDockerCompose(Applications application, DataBaseDto dataBaseDto);

    DockerDto createDockerCompose(Applications application);

    DockerDto startDockerCompose(Applications application);
}
