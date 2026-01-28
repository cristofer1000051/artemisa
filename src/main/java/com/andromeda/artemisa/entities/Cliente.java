package com.andromeda.artemisa.entities;

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

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Fattura> fatture;

    public List<Fattura> getFatture() {
        return fatture;
    }

    private Cliente(ClienteBuilder builder) {
        super(builder); // Passa i dati al padre
    }

    public static class ClienteBuilder extends Utente.Builder<Cliente, ClienteBuilder> {

        private String indirizzo;
        private String cap;
        private String citta;
        private String provincia;

        public ClienteBuilder indirizzo(String indirizzo) {
            this.indirizzo = indirizzo;
            return this;
        }

        public ClienteBuilder cap(String cap) {
            this.cap = cap;
            return this;
        }

        public ClienteBuilder citta(String citta) {
            this.citta = citta;
            return this;
        }

        public ClienteBuilder provincia(String provincia) {
            this.provincia = provincia;
            return this;
        }

        @Override
        protected ClienteBuilder self() {
            return this; // Qui ritorni "this" come ClienteBuilder
        }

        @Override
        public Cliente build() {
            this.rol = "client";
            return new Cliente(this);
        }
    }
}
