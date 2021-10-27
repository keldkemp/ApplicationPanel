package com.keldkemp.applicationpanel.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Map;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OptionsDto {
    @JsonProperty("is_create_db")
    private Boolean isCreateDb;
    @JsonProperty("is_start_app")
    private Boolean isStartApp;
    private Map<String, String> options;
}
