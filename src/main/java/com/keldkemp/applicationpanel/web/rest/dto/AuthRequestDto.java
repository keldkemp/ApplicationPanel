package com.keldkemp.applicationpanel.web.rest.dto;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String login;
    private String password;
}
