package com.andromeda.artemisa.security.dtos;

import java.sql.Timestamp;

public class TokenDto {

    private String token;
    private Timestamp expiration;

    public TokenDto(Timestamp expiration, String token) {
        this.expiration = expiration;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Timestamp getExpiration() {
        return expiration;
    }

    public void setExpiration(Timestamp expiration) {
        this.expiration = expiration;
    }
}
