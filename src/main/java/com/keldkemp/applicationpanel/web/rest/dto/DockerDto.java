package com.keldkemp.applicationpanel.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DockerDto {
    @JsonProperty("application_id")
    private Long applicationId;
    @JsonProperty("application_name")
    private String applicationName;
    @JsonProperty("application_original_name")
    private String applicationOriginalName;
    @JsonProperty("result")
    private String result;
    @JsonProperty("status")
    private Boolean status;
}
