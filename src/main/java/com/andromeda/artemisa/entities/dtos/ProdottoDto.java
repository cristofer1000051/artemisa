package com.andromeda.artemisa.entities.dtos;

import java.math.BigDecimal;

public class ProdottoDto {
    private Long id;
    private String nome;
    private Integer quantita;
    private BigDecimal prezzo;

    
    public ProdottoDto(Long id, String nome, BigDecimal prezzo, Integer quantita) {
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantita = quantita;
    }

    public ProdottoDto() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public void setPrezzo(BigDecimal prezzo) {
        this.prezzo = prezzo;
    }

}
