package com.andromeda.artemisa.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andromeda.artemisa.entities.TempData;


public interface TempDataRepository extends JpaRepository<TempData, Long>{
    Optional<TempData> findByKey(String key);
    void deleteByKey(String key);
    void deleteByKeyIn(List<String> keys);
}
