package com.keldkemp.applicationpanel.github;

import com.keldkemp.applicationpanel.github.domain.GitHubUserRepo;
import com.keldkemp.applicationpanel.github.domain.GitHubUserRepos;
import com.keldkemp.applicationpanel.github.domain.GitHubWorkflowsRepo;

public interface GitHubService {
    GitHubUserRepos getAllRepos();

    GitHubWorkflowsRepo getAllWorkflows(GitHubUserRepo userRepo);

    GitHubUserRepo getUserRepo(String fullName);
}
