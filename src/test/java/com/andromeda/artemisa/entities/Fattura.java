package com.andromeda.artemisa.entities;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "fatture")
public class Fattura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prezzo_totale")
    private double prezzoTotale = 0;
    @Column(name = "data_creazione")
    private Timestamp dataCreazione;

    @ManyToOne
    private Cliente cliente;

    public Fattura() {}

    public static Fattura init() {
        return new Fattura();
    }

    public Long getId() {
        return id;
    }

    public Fattura setId(Long id) {
        this.id = id;
        return this;
    }

    public double getPrezzoTotale() {
        return prezzoTotale;
    }

    public Fattura setPrezzoTotale(double prezzoTotale) {
        this.prezzoTotale = prezzoTotale;
        return this;
    }

    public Timestamp getDataCreazione() {
        return dataCreazione;
    }

    public Fattura setDataCreazione(Timestamp dataCreazione) {
        this.dataCreazione = dataCreazione;
        return this;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Fattura setCliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

}
