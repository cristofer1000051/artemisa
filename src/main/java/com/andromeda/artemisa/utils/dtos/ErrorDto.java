package com.andromeda.artemisa.utils.dtos;

import java.time.LocalDateTime;

public class ErrorDto {

    private String messaggio;
    private int codiceStatus;
    private LocalDateTime data;
    private String path;

    public ErrorDto(String messaggio, int codiceStatus, String path) {
        this.messaggio = messaggio;
        this.codiceStatus = codiceStatus;
        this.data = LocalDateTime.now();
        this.path = path;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public int getCodiceStatus() {
        return codiceStatus;
    }

    public LocalDateTime getData() {
        return data;
    }

    public String getPath() {
        return path;
    }

    public static class Builder {

        private String messaggio;
        private int codiceStatus;
        private String path;

        public Builder() {
        }

        public Builder messaggio(String messaggio) {
            this.messaggio = messaggio;
            return this;
        }

        public Builder codiceStatus(int codiceStatus) {
            this.codiceStatus = codiceStatus;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public ErrorDto build() {
            return new ErrorDto(this.messaggio, this.codiceStatus, this.path);
        }
    }
}
