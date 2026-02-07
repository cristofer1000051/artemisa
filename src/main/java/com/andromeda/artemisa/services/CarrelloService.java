package com.andromeda.artemisa.services;

import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andromeda.artemisa.entities.Prodotto;
import com.andromeda.artemisa.entities.dtos.ItemDto;

@Service
public class CarrelloService {

    private final RedisTemplate<String, Object> redisTemplate;

    public CarrelloService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //Ricordare che tutto dipendera del Id del prodDtoTemp
    @Transactional
    public void save(ItemDto itemDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();
        Long idProdotto = itemDto.getId();

        Object esistente = redisTemplate.opsForHash().get(chiaveCarrello, idProdotto);

        if (esistente != null) {
            ItemDto vecchioItem = (ItemDto) esistente;
            int nuovaQuantitaTotale = vecchioItem.getQuantita() + itemDto.getQuantita();
            vecchioItem.setQuantita(nuovaQuantitaTotale);
            redisTemplate.opsForHash().put(chiaveCarrello, idProdotto, vecchioItem);
        } else {
            redisTemplate.opsForHash().put(chiaveCarrello, idProdotto, itemDto);
        }

        redisTemplate.expire(chiaveCarrello, Duration.ofDays(1));
    }

    public void saveAll(List<ItemDto> itemDto) {
        for (ItemDto i : itemDto) {
            this.save(i);
        }
    }

    @Transactional
    public void deleteById(Long prodId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }
        String chiaveCarrello = "cart:" + authentication.getName();
        String hashKey = String.valueOf(prodId);
        redisTemplate.opsForHash().delete(chiaveCarrello, hashKey);
    }

    @Transactional
    public void deleteAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();
        redisTemplate.delete(chiaveCarrello);
    }

    public List<Prodotto> findAll() {
        return null;
    }
}
