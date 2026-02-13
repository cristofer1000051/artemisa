package com.andromeda.artemisa.entities.dtos;

import java.math.BigDecimal;

public interface ItemInt {

    public class ItemCarDto {

        private String codProdotto;
        private Integer quantita;

        public ItemCarDto(String codProdotto, Integer quantita) {
            this.codProdotto = codProdotto;
            this.quantita = quantita;
        }

        public String getCodProdotto() {
            return codProdotto;
        }

        public void setCodProdotto(String codProdotto) {
            this.codProdotto = codProdotto;
        }

        public Integer getQuantita() {
            return quantita;
        }

        public void setQuantita(Integer quantita) {
            this.quantita = quantita;
        }

    }

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

        public static class Builder {

            private String codProdotto;
            private String nome;
            private Integer quantita;
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

            public Builder quantita(Integer quantita) {
                this.quantita = quantita;
                return this;
            }

            public Builder prezzo(BigDecimal prezzo) {
                this.prezzo = prezzo;
                return this;
            }

            public Builder prezzoQta(BigDecimal prezzoQta) {
                this.prezzoQta = prezzoQta;
                return this;
            }

            public ItemDto build() {
                return new ItemDto(codProdotto, nome, quantita, prezzo, prezzoQta);
            }
        }

    }
}
