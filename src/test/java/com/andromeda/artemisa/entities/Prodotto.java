package com.andromeda.artemisa.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "prodotti")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private int quantita;
    private double prezzo;
    @ManyToMany
    @JoinTable(
            name = "prodotti_categorie",
            joinColumns = @JoinColumn(name = "prodotto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorie;

    public Prodotto() {
        this.categorie = new ArrayList<>();
    }

    public static Prodotto init() {
        return new Prodotto();
    }

    public Long getId() {
        return id;
    }

    public Prodotto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getNome() {
        return nome;
    }

    public Prodotto setNome(String nome) {
        this.nome = nome;
        return this;
    }

    public int getQuantita() {
        return quantita;
    }

    public Prodotto setQuantita(int quantita) {
        this.quantita = quantita;
        return this;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public Prodotto setPrezzo(double prezzo) {
        this.prezzo = prezzo;
        return this;
    }

    public List<Categoria> getCategorie() {
        return categorie;
    }

    public Prodotto setCategorie(List<Categoria> categorie) {
        this.categorie = categorie;
        return this;
    }

    

}
