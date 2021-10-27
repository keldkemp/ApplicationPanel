package com.keldkemp.applicationpanel.web.rest.dto;

import lombok.Data;

@Data
public class DataBaseDto {
    private String dbName;
    private DataBaseRoleDto role;
    private String url;
}
