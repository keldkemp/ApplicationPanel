package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.Applications;
import com.keldkemp.applicationpanel.web.rest.dto.ApplicationDto;
import com.keldkemp.applicationpanel.web.rest.dto.DataBaseDto;
import com.keldkemp.applicationpanel.web.rest.dto.DockerDto;
import com.keldkemp.applicationpanel.web.rest.dto.FileDto;

import java.util.List;

public interface ApplicationsService {
    List<ApplicationDto> getAllMyApplications();

    Applications getApplicationById(Long id);

    ApplicationDto getApplicationDtoById(Long id);

    ApplicationDto getApplicationDtoByOriginalName(String name);

    ApplicationDto addApplication(ApplicationDto applicationDto);

    ApplicationDto editApplication(ApplicationDto applicationDto);

    Applications editApplication(Applications application);

    DockerDto deployApp(ApplicationDto applicationDto);

    DockerDto deployApp(ApplicationDto applicationDto, DataBaseDto dataBase);

    DockerDto updateApp(Applications application);

    void autoUpdateApp();

    void cancelUpdate(ApplicationDto applicationDto);

    List<FileDto> getAllFiles(Long id);

    FileDto getFileBody(Long id, String fileName);

    FileDto writeFile(FileDto file);
}
