package com.andromeda.artemisa.entities.dtos;

import java.math.BigDecimal;

public class ItemDto {

    private String codProdotto;
    private String nome;
    private Integer quantita;
    private BigDecimal prezzo;
    private BigDecimal prezzoQta;

    public ItemDto(String codProdotto, String nome, Integer quantita, BigDecimal prezzo, BigDecimal prezzoQta) {
        this.codProdotto = codProdotto;
        this.nome = nome;
        this.quantita = quantita;
        this.prezzo = prezzo;
        this.prezzoQta = prezzoQta;
    }

    public ItemDto() {
    }

    public String getCodProdotto() {
        return codProdotto;
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

    public BigDecimal getPrezzoQta() {
        return prezzoQta;
    }

    public void setCodProdotto(String codProdotto) {
        this.codProdotto = codProdotto;
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

    public void setPrezzoQta(BigDecimal prezzoQta) {
        this.prezzoQta = prezzoQta;
    }

    

}
