package com.andromeda.artemisa.services;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andromeda.artemisa.entities.dtos.ProdottoDto;

@Service
public class CarrelloService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final long MAX_ITEMS_PER_CART = 50;

    public CarrelloService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //Ricordare che tutto dipendera del Id del prodDtoTemp
    @Transactional
    public void save(ProdottoDto prodottoDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();
        String idProdotto = prodottoDto.getId().toString();
        System.out.println("Ricevuto DTO: " + prodottoDto.getId());
        Object esistente = redisTemplate.opsForHash().get(chiaveCarrello, idProdotto);

        if (esistente != null) {
            ProdottoDto vecchioItem = (ProdottoDto) esistente;
            int nuovaQuantitaTotale = vecchioItem.getQuantita() + prodottoDto.getQuantita();
            vecchioItem.setQuantita(nuovaQuantitaTotale);
            redisTemplate.opsForHash().put(chiaveCarrello, idProdotto, vecchioItem);
        } else {
            Long cant = redisTemplate.opsForHash().size(chiaveCarrello);
            if (cant != null && cant >= MAX_ITEMS_PER_CART) {
                throw new RuntimeException("Il carrello è pieno! Non puoi aggiungere più di " + MAX_ITEMS_PER_CART + " prodotti diversi.");
            }
            redisTemplate.opsForHash().put(chiaveCarrello, idProdotto, prodottoDto);
        }
        redisTemplate.expire(chiaveCarrello, Duration.ofDays(1));
    }

    public void saveAll(List<ProdottoDto> prodottoDto) {
        for (ProdottoDto i : prodottoDto) {
            this.save(i);
        }
    }

    public void deleteById(Long prodId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }
        String chiaveCarrello = "cart:" + authentication.getName();
        redisTemplate.opsForHash().delete(chiaveCarrello, prodId);
    }

    public void deleteAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();
        redisTemplate.delete(chiaveCarrello);
    }

    public List<ProdottoDto> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();
        List<ProdottoDto> prodottiDto = redisTemplate.opsForHash().values(chiaveCarrello).stream().map(p -> (ProdottoDto) p).collect(Collectors.toList());
        return prodottiDto;
    }
}
