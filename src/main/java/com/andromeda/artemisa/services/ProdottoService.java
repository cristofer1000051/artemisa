package com.andromeda.artemisa.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.andromeda.artemisa.entities.Categoria;
import com.andromeda.artemisa.entities.Prodotto;
import com.andromeda.artemisa.repositories.ProdottoRepository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;

@Service
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;

    public ProdottoService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }

    public Page<Prodotto> reperireProdottiPageable(String nome, String categoria, BigDecimal prezzoMin, BigDecimal prezzoMax, Pageable pageable) {
        Specification<Prodotto> filtro = (root, query, cb) -> {
            /**
             * cb operatori logici matematici root la tabella query, la query
             * che stiamo per inviare
             */
            List<Predicate> predicates = new ArrayList<>();
            //1. Controllo filtro nome prodotto
            if (nome != null && !nome.isEmpty()) {
                predicates.add(cb.like(root.get("nome"), "%" + nome + "%"));
            }
            //2. Controllo filtro categoria (con join)
            if (categoria != null && !categoria.isEmpty()) {
                Join<Prodotto, Categoria> categoriaJoin = root.join("categoria");
                predicates.add(cb.like(categoriaJoin.get("nome"), "%" + categoria + "%"));
            }
            if (prezzoMin != null && prezzoMax != null) {
                if (prezzoMin.compareTo(BigDecimal.ZERO) >= 0 && prezzoMax.compareTo(prezzoMin) >= 0) {
                    predicates.add(cb.between(root.get("prezzo"), prezzoMin, prezzoMax));
                }
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        Page<Prodotto> pageProdotto = prodottoRepository.findAll(filtro, pageable);
        return pageProdotto;
    }
}
