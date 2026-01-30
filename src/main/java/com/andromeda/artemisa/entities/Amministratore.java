package com.andromeda.artemisa.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("amministratori")
public class Amministratore extends Utente {

    public Amministratore(AmminBuilder builder) {
        super(builder);
    }

    public static class AmminBuilder extends Utente.Builder<Amministratore, AmminBuilder> {

        @Override
        protected AmminBuilder self() {
            return this; // Qui ritorni "this" come AmminBuilder
        }

        @Override
        public Amministratore build() {
            this.rol = "admin";
            return new Amministratore(this);
        }

    }
}
