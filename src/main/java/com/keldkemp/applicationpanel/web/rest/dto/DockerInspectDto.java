package com.keldkemp.applicationpanel.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class DockerInspectDto {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Created")
    private String created;
    @JsonProperty("State")
    private DockerStateDto state;
}
