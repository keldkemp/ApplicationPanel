package com.keldkemp.applicationpanel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class SettingsServiceImpl implements SettingsService {

    @Autowired
    private Environment env;

    @Value("${gitHub.token}")
    private String gitHubToken;

    @Value("${projectsFolderPath}")
    private String projectsFolderPath;

    @Override
    public String getGitHubToken() {
        return gitHubToken;
    }

    @Override
    public String getProjectsFolderPath() {
        if (projectsFolderPath.endsWith("/") || projectsFolderPath.endsWith("\\"))
        {
            return projectsFolderPath;
        } else {
            return projectsFolderPath + "/";
        }
    }
}
