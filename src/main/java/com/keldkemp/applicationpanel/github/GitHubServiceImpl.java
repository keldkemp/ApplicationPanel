package com.keldkemp.applicationpanel.github;

import com.keldkemp.applicationpanel.github.domain.*;
import com.keldkemp.applicationpanel.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GitHubServiceImpl implements GitHubService {
    public static final String GITHUB_URL = "https://api.github.com";

    public static final String GITHUB_PATH_USER_REPOS = "/user/repos?affiliation=owner";

    public static final String GITHUB_PATH_USER_REPO = "/repos/{name}";

    public static final String GITHUB_PATH_WORKFLOWS_REPOS = "/repos/{name}/actions/workflows";

    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private SettingsService settingsService;

    @Override
    public GitHubUserRepo getUserRepo(String fullName) {
        return userRepo(fullName);
    }

    @Override
    public GitHubWorkflowsRepo getAllWorkflows(GitHubUserRepo userRepo) {
        return workflowsRepos(userRepo);
    }

    @Override
    public GitHubUserRepos getAllRepos() {
        GitHubUserRepos gitHubUserRepos = new GitHubUserRepos();
        gitHubUserRepos.setGitHubUserRepoList(userRepos());
        return gitHubUserRepos;
    }

    private GitHubUserRepo userRepo(String fullName) {
        String finalUrl = GITHUB_URL + GITHUB_PATH_USER_REPO.replace("{name}", fullName);
        HttpEntity<GitHubQuery> gitHubQueryHttpEntity = buildHttpEntity("", 30);

        return getRestTemplate().exchange(
                        finalUrl,
                        HttpMethod.GET,
                        gitHubQueryHttpEntity,
                        new ParameterizedTypeReference<GitHubUserRepo>() {
                        })
                .getBody();
    }

    private GitHubWorkflowsRepo workflowsRepos(GitHubUserRepo userRepo) {
        String finalUrl = GITHUB_URL + GITHUB_PATH_WORKFLOWS_REPOS.replace("{name}", userRepo.getFullName());
        HttpEntity<GitHubQuery> gitHubQueryHttpEntity = buildHttpEntity("", 30);

        return getRestTemplate().exchange(
                        finalUrl,
                        HttpMethod.GET,
                        gitHubQueryHttpEntity,
                        new ParameterizedTypeReference<GitHubWorkflowsRepo>() {
                        })
                .getBody();
    }

    private List<GitHubUserRepo> userRepos() {
        String finalUrl = GITHUB_URL + GITHUB_PATH_USER_REPOS;
        HttpEntity<GitHubQuery> gitHubQueryHttpEntity = buildHttpEntity("", 30);

        return getRestTemplate().exchange(
                        finalUrl,
                        HttpMethod.GET,
                        gitHubQueryHttpEntity,
                        new ParameterizedTypeReference<List<GitHubUserRepo>>() {
                        })
                .getBody();
    }

    private HttpEntity<GitHubQuery> buildHttpEntity(String query, Integer count) {
        String token = settingsService.getGitHubToken();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(new GitHubQuery(query, count), httpHeaders);
    }

    private synchronized RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
