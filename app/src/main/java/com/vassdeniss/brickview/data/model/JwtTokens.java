package com.vassdeniss.brickview.data.model;

public class JwtTokens {
    private String accessToken;
    private String refreshToken;

    public JwtTokens(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
