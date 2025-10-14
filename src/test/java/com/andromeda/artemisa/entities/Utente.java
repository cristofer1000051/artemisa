package com.andromeda.artemisa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "utenti")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_utente", discriminatorType = DiscriminatorType.STRING)
abstract public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String name;
    @Column(name = "cod_fiscale")
    protected String codFiscale;
    protected String cellulare;

    public Utente() {

    }

    public Long getId() {
        return id;
    }

    public Utente setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Utente setName(String name) {
        this.name = name;
        return this;
    }

    public String getCodFiscale() {
        return codFiscale;
    }

    public Utente setCodFiscale(String codFiscale) {
        this.codFiscale = codFiscale;
        return this;
    }

    public String getCellulare() {
        return cellulare;
    }

    public Utente setCellulare(String cellulare) {
        this.cellulare = cellulare;
        return this;
    }

}
