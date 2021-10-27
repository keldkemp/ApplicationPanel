package com.keldkemp.applicationpanel.github.domain;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GitHubUserRepos implements GitHubObject {
    public List<GitHubUserRepo> gitHubUserRepoList;
}
