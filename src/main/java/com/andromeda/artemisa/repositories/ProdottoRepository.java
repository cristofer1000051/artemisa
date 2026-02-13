package com.andromeda.artemisa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.andromeda.artemisa.entities.Prodotto;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long>, JpaSpecificationExecutor<Prodotto> {

    @Query("SELECT p.nome FROM Prodotto p WHERE p.nome IN :nomi")
    List<String> findNomeEsistenti(@Param("nomi") List<String> nomi);

    List<Prodotto> findBycodProdottoIn(@Param("codProdottList") List<String> codProdottoList);

    @Query("SELECT p from Prodotto p WHERE p.codProdotto = :codProdotto")
    Optional<Prodotto> findByCodProdotto(@Param("codProdotto") String codProdotto);
}
