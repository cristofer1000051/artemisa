package com.andromeda.artemisa.entities;

import org.springframework.security.crypto.password.PasswordEncoder;

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
import jakarta.persistence.Transient;

@Entity
@Table(name = "utenti")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_utente", discriminatorType = DiscriminatorType.STRING)
abstract public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String nome;
    protected String cognome;
    @Column(name = "cod_fiscale")
    protected String codFiscale;
    protected String cellulare;
    protected String email;
    @Column(name = "hash_password")
    protected String hashPassword;
    protected String rol;
    @Transient
    protected String password;

    protected Utente(Builder<?, ?> builder) {
        this.id = builder.id;
        this.nome = builder.nome;
        this.codFiscale = builder.codFiscale;
        this.cellulare = builder.cellulare;
        this.email = builder.email;
        this.cognome = builder.cognome;
        this.password = builder.password;
        this.rol = builder.rol;
    }

    public Utente() {
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getCodFiscale() {
        return codFiscale;
    }

    public String getCellulare() {
        return cellulare;
    }

    public String getEmail() {
        return email;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public String getRol() {
        return rol;
    }

    public void generateHashPassword(PasswordEncoder encoder) {
        if (this.password != null && encoder != null) {
            this.hashPassword = encoder.encode(this.password);
        }
    }

    public static abstract class Builder<T extends Utente, B extends Builder<T, B>> {

        protected Long id;
        protected String nome;
        protected String codFiscale;
        protected String cellulare;
        protected String email;
        protected String cognome;
        protected String rol;
        protected String password;

        // Metodo helper per ritornare "this" col tipo corretto (B)
        protected abstract B self();

        // Metodo astratto che le sottoclassi dovranno implementare
        public abstract T build();

        public B id(Long id) {
            this.id = id;
            return self();
        }

        public B nome(String nome) {
            this.nome = nome;
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

        public B cognome(String cognome) {
            this.cognome = cognome;
            return self();
        }

        protected B rol(String rol) {
            this.rol = rol;
            return self();
        }

        public B password(String password) {
            this.password = password;
            return self();
        }

    }

}
