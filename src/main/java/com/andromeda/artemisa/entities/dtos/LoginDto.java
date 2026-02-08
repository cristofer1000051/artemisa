package com.andromeda.artemisa.entities.dtos;

import java.util.List;

public class LoginDto {

    private String email;
    private String password;
    private List<ItemDto> carrelloLocale;

    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ItemDto> getCarrelloLocale() {
        return carrelloLocale;
    }

    public void setCarrelloLocale(List<ItemDto> carrelloLocale) {
        this.carrelloLocale = carrelloLocale;
    }

}
