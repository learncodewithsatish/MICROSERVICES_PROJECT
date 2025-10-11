package com.auth.model;

public class JwtTokenResponse {

    private String token;
    private String type;
    private String validUntil;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(String validUntil) {
        this.validUntil = validUntil;
    }

    @Override
    public String toString() {
        return "JwtTokenResponse{" +
                "token='" + token + '\'' +
                ", type='" + type + '\'' +
                ", validUntil='" + validUntil + '\'' +
                '}';
    }
}
