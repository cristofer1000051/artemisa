package com.andromeda.artemisa.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fatture_prodotti")
public class FatturaProdotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantita;

    @ManyToOne
    private Fattura fattura;

    @ManyToOne
    private Prodotto prodotto;

    public FatturaProdotto() {
    }

    public FatturaProdotto( Long id, Integer quantita,Fattura fattura, Prodotto prodotto) {
        this.fattura = fattura;
        this.id = id;
        this.prodotto = prodotto;
        this.quantita = quantita;
    }

    public Long getId() {
        return id;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public Fattura getFattura() {
        return fattura;
    }

    public Prodotto getProdotto() {
        return prodotto;
    }

    public static class Builder {

        private Long id;
        private Integer quantita;
        private Fattura fattura;
        private Prodotto prodotto;

        public Builder(){

        }
        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder quantita(Integer quantita) {
            this.quantita = quantita;
            return this;
        }

        public Builder fattura(Fattura fattura) {
            this.fattura = fattura;
            return this;
        }

        public Builder prodotto(Prodotto prodotto) {
            this.prodotto = prodotto;
            return this;
        }
        public FatturaProdotto build(){
            FatturaProdotto FatturaProdotto = new FatturaProdotto(this.id,this.quantita,this.fattura,this.prodotto);
            return FatturaProdotto;
        }

    }
}
