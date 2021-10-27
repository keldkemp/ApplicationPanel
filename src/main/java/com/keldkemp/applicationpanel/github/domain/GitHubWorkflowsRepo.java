package com.keldkemp.applicationpanel.github.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GitHubWorkflowsRepo implements GitHubObject {
    @JsonProperty("total_count")
    private Integer totalCount;
    @JsonProperty("workflows")
    private List<GitHubWorkflow> workflows;
}
