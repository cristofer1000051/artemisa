package com.andromeda.artemisa.entities;

import java.util.ArrayList;
import java.util.List;

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
    private String name;

    @ManyToMany(mappedBy = "categorie")
    private List<Prodotto> prodotti;

    public Categoria() {
        prodotti = new ArrayList<>();
    }

    public static Categoria init() {
        return new Categoria();
    }

    public Long getId() {
        return id;
    }

    public Categoria setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Categoria setName(String name) {
        this.name = name;
        return this;
    }

    public List<Prodotto> getProdotti() {
        return prodotti;
    }

    public Categoria setProdotti(List<Prodotto> prodotti) {
        this.prodotti = prodotti;
        return this;
    }

}
