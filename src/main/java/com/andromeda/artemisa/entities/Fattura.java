package com.andromeda.artemisa.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "fatture")
public class Fattura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prezzo_totale")
    private BigDecimal prezzoTotale;
    @Column(name = "data_creazione")
    private Timestamp dataCreazione;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "fattura", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FatturaProdotto> prodotti;

    public Fattura(Cliente cliente, Timestamp dataCreazione, Long id, BigDecimal prezzoTotale, List<FatturaProdotto> prodotti) {
        this.cliente = cliente;
        this.dataCreazione = dataCreazione;
        this.id = id;
        this.prezzoTotale = prezzoTotale;
        this.prodotti = prodotti;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrezzoTotale() {
        return prezzoTotale;
    }

    public Timestamp getDataCreazione() {
        return dataCreazione;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<FatturaProdotto> getProdotti() {
        return prodotti;
    }

    public static class Builder {

        private Long id;
        private BigDecimal prezzoTotale;
        private Timestamp dataCreazione;
        private Cliente cliente;
        private List<FatturaProdotto> prodotti;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder prezzoTotale(BigDecimal prezzoTotale) {
            this.prezzoTotale = prezzoTotale;
            return this;
        }

        public Builder dataCreazione(Timestamp dataCreazione) {
            this.dataCreazione = dataCreazione;
            return this;
        }

        public Builder cliente(Cliente cliente) {
            this.cliente = cliente;
            return this;
        }

        public Builder prodotti(List<FatturaProdotto> prodotti) {
            this.prodotti = prodotti;
            return this;
        }

        public Fattura build(){
            Fattura fattura = new Fattura(cliente, dataCreazione, id, prezzoTotale, prodotti);
            return fattura;
        }
    }

    

}
