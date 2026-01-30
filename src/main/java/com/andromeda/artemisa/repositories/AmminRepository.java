package com.andromeda.artemisa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andromeda.artemisa.entities.Amministratore;

public interface AmminRepository extends JpaRepository<Amministratore, Long>{

}
