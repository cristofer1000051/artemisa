package com.andromeda.artemisa.entities;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "categorie")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String nome;

    @ManyToMany(mappedBy = "categorie")
    private List<Prodotto> prodotti;

    public Categoria(Long id, String nome, List<Prodotto> prodotti) {
        this.id = id;
        this.nome = nome;
        this.prodotti = prodotti;
    }

    public Long getId() {
        return id;
    }

    public String getnome() {
        return nome;
    }

    public List<Prodotto> getProdotti() {
        return prodotti;
    }

    public static class Builder {

        private Long id;
        private String nome;
        private List<Prodotto> prodotti;

        public Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder nome(String nome) {
            this.nome = nome;
            return this;
        }

        public Builder prodotti(List<Prodotto> prodotti) {
            this.prodotti = prodotti;
            return this;
        }

        public Categoria build() {
            Categoria categoria = new Categoria(this.id, this.nome, this.prodotti);
            return categoria;
        }
    }

}
