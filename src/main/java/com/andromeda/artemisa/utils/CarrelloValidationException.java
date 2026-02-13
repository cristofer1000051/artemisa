package com.andromeda.artemisa.utils;

import java.util.List;

public class CarrelloValidationException extends RuntimeException{
    private final List<String> errori;

    public CarrelloValidationException(List<String> errori) {
        super("Ci sono errori nel carrello");
        this.errori = errori;
    }

    public List<String> getErrori() {
        return errori;
    }
}
