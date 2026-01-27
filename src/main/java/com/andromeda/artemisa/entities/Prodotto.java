package com.andromeda.artemisa.entities;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "prodotti")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Integer quantita;
    private BigDecimal prezzo;
    
    @ManyToMany
    @JoinTable(
            name = "prodotti_categorie",
            joinColumns = @JoinColumn(name = "prodotto_id"),
            inverseJoinColumns = @JoinColumn(name = "categoria_id")
    )
    private List<Categoria> categorie;

    @OneToMany(mappedBy = "prodotto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FatturaProdotto> fattureProdotti;

    public Prodotto(List<Categoria> categorie, List<FatturaProdotto> fattureProdotti, Long id, String nome, BigDecimal prezzo, int quantita) {
        this.categorie = categorie;
        this.fattureProdotti = fattureProdotti;
        this.id = id;
        this.nome = nome;
        this.prezzo = prezzo;
        this.quantita = quantita;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public BigDecimal getPrezzo() {
        return prezzo;
    }

    public List<Categoria> getCategorie() {
        return categorie;
    }

    public List<FatturaProdotto> getFattureProdotti() {
        return fattureProdotti;
    }

    public static class Builder {

        private Long id;
        private String nome;
        private Integer quantita;
        private BigDecimal prezzo;
        private List<Categoria> categorie;
        private List<FatturaProdotto> fattureProdotti;

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

        public Builder quantita(Integer quantita) {
            this.quantita = quantita;
            return this;
        }

        public Builder prezzo(BigDecimal prezzo) {
            this.prezzo = prezzo;
            return this;
        }

        public Builder categorie(List<Categoria> categorie) {
            this.categorie = categorie;
            return this;
        }

        public Builder fattureProdotti(List<FatturaProdotto> fattureProdotti) {
            this.fattureProdotti = fattureProdotti;
            return this;
        }

        public Prodotto build() {
            Prodotto fatturaProdotto = new Prodotto(this.categorie, this.fattureProdotti, this.id, this.nome, this.prezzo, this.quantita);
            return fatturaProdotto;
        }
    }

}
