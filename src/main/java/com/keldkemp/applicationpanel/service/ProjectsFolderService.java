package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.Applications;
import com.keldkemp.applicationpanel.web.rest.dto.FileDto;

import java.util.List;

public interface ProjectsFolderService {

    void createAppFolder(Applications application);

    void writeToFile(Applications application, String fileName, String message);

    FileDto readFile(Applications application, String fileName);

    List<FileDto> getAllFilesInApp(Applications application);
}
