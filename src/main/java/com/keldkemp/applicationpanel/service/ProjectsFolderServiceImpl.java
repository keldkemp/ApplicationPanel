package com.keldkemp.applicationpanel.service;

import com.keldkemp.applicationpanel.models.Applications;
import com.keldkemp.applicationpanel.util.StringUtil;
import com.keldkemp.applicationpanel.web.rest.dto.FileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProjectsFolderServiceImpl implements ProjectsFolderService {

    @Autowired
    private SettingsService settingsService;

    @Override
    public void createAppFolder(Applications application) {
        String path = settingsService.getProjectsFolderPath();
        Boolean result = new File(path + application.getOriginalName()).mkdirs();
    }

    @Override
    public void writeToFile(Applications application, String fileName, String message) {
        String path = StringUtil.getPath(settingsService.getProjectsFolderPath(), application.getOriginalName());

        if (!checkIsCreateDir(path)) {
            createAppFolder(application);
        }
        try {
            FileWriter file = new FileWriter(path + fileName);
            file.write(message);
            file.close();
        } catch (IOException e) {
            //TODO: return Exception
            System.out.println("ALARM");
        }
    }

    @Override
    public FileDto readFile(Applications application, String fileName) {
        Path path = Paths.get(StringUtil.getPath(settingsService.getProjectsFolderPath(), application.getOriginalName()) + fileName);
        try {
            Stream<String> lines = Files.lines(path);
            String data = lines.collect(Collectors.joining("\n"));
            lines.close();
            FileDto file = new FileDto();
            file.setPath(path.toString());
            file.setFileName(fileName);
            file.setApplicationId(application.getId());
            file.setText(data);
            return file;
        } catch (Exception e) {
            //TODO: return Exception
            System.out.println("ALARM");
            throw new NullPointerException("1");
        }
    }

    @Override
    public List<FileDto> getAllFilesInApp(Applications application) {
        String path = StringUtil.getPath(settingsService.getProjectsFolderPath(), application.getOriginalName());
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            return paths
                    .filter(Files::isRegularFile)
                    .map(file -> {
                        FileDto dto = new FileDto();
                        dto.setFileName(file.getFileName().toString());
                        dto.setPath(path);
                        dto.setApplicationId(application.getId());
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            //TODO: return Exception
            System.out.println("ALARM");
            throw new NullPointerException("1");
        }
    }

    private Boolean checkIsCreateDir(String path) {
        return new File(path).exists();
    }
}
