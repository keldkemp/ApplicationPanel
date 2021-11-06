package com.keldkemp.applicationpanel.web.rest.dto;

public class RefreshTokenRequestDto {
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
