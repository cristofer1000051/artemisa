package com.andromeda.artemisa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andromeda.artemisa.entities.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

}
