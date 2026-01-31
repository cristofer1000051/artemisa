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

    public static class ProdottoBuilder {

        private Long id;
        private String nome;
        private Integer quantita;
        private BigDecimal prezzo;

        public ProdottoBuilder() {
        }


        public ProdottoBuilder(Long id, String nome, BigDecimal prezzo, Integer quantita) {
            this.id = id;
            this.nome = nome;
            this.prezzo = prezzo;
            this.quantita = quantita;
        }

        public ProdottoBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ProdottoBuilder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public ProdottoBuilder quantita(Integer quantita) {
            this.quantita = quantita;
            return this;
        }

        public ProdottoBuilder prezzo(BigDecimal prezzo) {
            this.prezzo = prezzo;
            return this;
        }

        public ProdottoDto build() {
            return new ProdottoDto(this.id, this.nome, this.prezzo, this.quantita);
        }
    }
}
