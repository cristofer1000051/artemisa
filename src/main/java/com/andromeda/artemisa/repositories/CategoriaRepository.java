package com.andromeda.artemisa.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.andromeda.artemisa.entities.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
    @Query("SELECT c.nome FROM Categoria c WHERE c.nome IN :nomi")
    List<String> findNomeEsistenti(@Param ("nomi") List<String> nomi);

    
}
