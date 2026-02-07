package com.andromeda.artemisa.entities.dtos;

public class ItemDto {
    private Long id;
    private Integer quantita;

    public ItemDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }


}
