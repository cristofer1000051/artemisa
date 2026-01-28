package com.andromeda.artemisa.security.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.andromeda.artemisa.entities.Utente;

@Repository
public interface AuthRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByEmail(String email);
}
