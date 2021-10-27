package com.keldkemp.applicationpanel.web.rest;

import com.keldkemp.applicationpanel.models.Applications;
import com.keldkemp.applicationpanel.service.ApplicationsService;
import com.keldkemp.applicationpanel.service.DockerService;
import com.keldkemp.applicationpanel.web.rest.dto.ApplicationDto;
import com.keldkemp.applicationpanel.web.rest.dto.DockerDto;
import com.keldkemp.applicationpanel.web.rest.dto.DockerInspectDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications/docker")
public class DockerController {

    @Autowired
    private DockerService dockerService;

    @Autowired
    private ApplicationsService applicationsService;

    @GetMapping("/{id}/inspect")
    public DockerInspectDto dockerInspect(@PathVariable Long id) {
        return dockerService.inspect(getApp(id));
    }

    @GetMapping("/{id}/container/stop")
    public DockerDto dockerStop(@PathVariable Long id) {
        return dockerService.stop(getApp(id));
    }

    @GetMapping("/{id}/container/start")
    public DockerDto dockerStart(@PathVariable Long id) {
        return dockerService.start(getApp(id));
    }

    @GetMapping("/{id}/start")
    public DockerDto appStart(@PathVariable Long id) {
        return dockerService.startDockerCompose(getApp(id));
    }

    private Applications getApp(Long id) {
        return applicationsService.getApplicationById(id);
    }
}
