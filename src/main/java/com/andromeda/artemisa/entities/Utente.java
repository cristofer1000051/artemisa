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
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_utente", discriminatorType = DiscriminatorType.STRING)
abstract public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String name;
    @Column(name = "cod_fiscale")
    protected String codFiscale;
    protected String cellulare;
    protected String email;
    @Column(name = "hash_password")
    protected String hashPassword;

    protected Utente(Builder<?, ?> builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.codFiscale = builder.codFiscale;
        this.cellulare = builder.cellulare;
        this.email = builder.email;
        this.hashPassword = builder.hashPassword;
    }

    public static abstract class Builder<T extends Utente, B extends Builder<T, B>> {

        protected Long id;
        protected String name;
        protected String codFiscale;
        protected String cellulare;
        protected String email;
        protected String hashPassword;

        // Metodo helper per ritornare "this" col tipo corretto (B)
        protected abstract B self();

        // Metodo astratto che le sottoclassi dovranno implementare
        public abstract T build();

        public B id(Long id) {
            this.id = id;
            return self();
        }

        public B name(String name) {
            this.name = name;
            return self();
        }

        public B codFiscale(String codFiscale) {
            this.codFiscale = codFiscale;
            return self();
        }

        public B cellulare(String cellulare) {
            this.cellulare = cellulare;
            return self();
        }

        public B email(String email) {
            this.email = email;
            return self();
        }

        public B hashPassword(String hashPassword){
            this.hashPassword = hashPassword;
            return self();
        }

    }
}
