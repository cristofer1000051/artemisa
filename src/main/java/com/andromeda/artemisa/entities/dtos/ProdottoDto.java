package com.andromeda.artemisa.entities.dtos;

import java.math.BigDecimal;

public class ProdottoDto {

    private String codProdotto;
    private String nome;
    private Integer stock;
    private BigDecimal prezzo;
    public ProdottoDto(String codProdotto, String nome, BigDecimal prezzo, Integer stock) {
        this.codProdotto = codProdotto;
        this.nome = nome;
        this.prezzo = prezzo;
        this.stock = stock;
    }

    public ProdottoDto() {
    }

    public String getCodProdotto() {
        return codProdotto;
    }

    public String getNome() {
        return nome;
    }

    public Integer getStock() {
        return stock;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }
    public static class Builder {

        private String codProdotto;
        private String nome;
        private Integer stock;
        private BigDecimal prezzo;
        private BigDecimal prezzoQta;
        public Builder() {
        }

        public Builder codProdotto(String codProdotto) {
            this.codProdotto = codProdotto;
            return this;
        }

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder stock(Integer stock) {
            this.stock = stock;
            return this;
        }

        public Builder prezzo(BigDecimal prezzo) {
            this.prezzo = prezzo;
            return this;
        }

        public ProdottoDto build() {
            return new ProdottoDto(codProdotto, nome, prezzo, stock);
        }

    }

}
