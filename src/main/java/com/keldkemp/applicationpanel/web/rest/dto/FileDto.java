package com.keldkemp.applicationpanel.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class FileDto {
    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("path")
    private String path;
    @JsonProperty("application_id")
    private Long applicationId;
    @JsonProperty("text")
    private String text;
}
