package com.keldkemp.applicationpanel.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DockerStateDto {
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Running")
    private Boolean running;
    @JsonProperty("Paused")
    private Boolean paused;
    @JsonProperty("Restarting")
    private Boolean restarting;
    @JsonProperty("OOMKilled")
    private Boolean oomKilled;
    @JsonProperty("Dead")
    private Boolean dead;
    @JsonProperty("Pid")
    private Integer pid;
    @JsonProperty("ExitCode")
    private Integer exitCode;
    @JsonProperty("Error")
    private String error;
    @JsonProperty("StartedAt")
    private String startedAt;
    @JsonProperty("FinishedAt")
    private String finishedAt;
}
