package com.andromeda.artemisa.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

@Entity
@DiscriminatorValue("clienti")
public class Cliente extends Utente {

    private String indirizzo;
    private String cap;
    private String citta;
    private String provincia;
    private String mail;
    private String password;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Fattura> fatture;

    public List<Fattura> getFatture() {
        return fatture;
    }

    public Cliente setFatture(List<Fattura> fatture) {
        this.fatture = fatture;
        return this;
    }

    public Cliente() {
        this.fatture = new ArrayList<>();
    }

    public static Cliente init() {
        return new Cliente();
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public Cliente setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
        return this;
    }

    public String getCap() {
        return cap;
    }

    public Cliente setCap(String cap) {
        this.cap = cap;
        return this;
    }

    public String getCitta() {
        return citta;
    }

    public Cliente setCitta(String citta) {
        this.citta = citta;
        return this;
    }

    public String getProvincia() {
        return provincia;
    }

    public Cliente setProvincia(String provincia) {
        this.provincia = provincia;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public Cliente setMail(String mail) {
        this.mail = mail;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public Cliente setPassword(String password) {
        this.password = password;
        return this;
    }

}
