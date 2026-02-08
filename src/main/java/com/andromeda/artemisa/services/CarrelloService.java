package com.andromeda.artemisa.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andromeda.artemisa.entities.dtos.CarrelloDto;
import com.andromeda.artemisa.entities.dtos.ItemDto;

@Service
public class CarrelloService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final long MAX_ITEMS_PER_CART = 50;

    public CarrelloService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //Ricordare che tutto dipendera del Id del prodDtoTemp
    @Transactional
    public void save(ItemDto itemDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();
        String codProd = itemDto.getCodProdotto();

        Object esistente = redisTemplate.opsForHash().get(chiaveCarrello, codProd);

        if (esistente != null) {
            ItemDto vecchioItem = (ItemDto) esistente;
            vecchioItem.setQuantita(vecchioItem.getQuantita() + itemDto.getQuantita());
            vecchioItem.setPrezzoQta(itemDto.getPrezzo().multiply(BigDecimal.valueOf(vecchioItem.getQuantita())));
            redisTemplate.opsForHash().put(chiaveCarrello, codProd, vecchioItem);
        } else {
            Long cant = redisTemplate.opsForHash().size(chiaveCarrello);
            if (cant != null && cant >= MAX_ITEMS_PER_CART) {
                throw new RuntimeException("Il carrello è pieno! Non puoi aggiungere più di " + MAX_ITEMS_PER_CART + " prodotti diversi.");
            }
            redisTemplate.opsForHash().put(chiaveCarrello, codProd, itemDto);
        }
        redisTemplate.expire(chiaveCarrello, Duration.ofDays(1));
    }

    public void saveAll(List<ItemDto> itemDtoList) {
        for (ItemDto i : itemDtoList) {
            this.save(i);
        }
    }

    public void deleteById(Long codProd) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return;
        }
        String chiaveCarrello = "cart:" + authentication.getName();
        redisTemplate.opsForHash().delete(chiaveCarrello, codProd);
    }

    public void deleteAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();
        redisTemplate.delete(chiaveCarrello);
    }

    public CarrelloDto findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();
        List<ItemDto> itemDtoList = redisTemplate.opsForHash().values(chiaveCarrello).stream().map(p -> (ItemDto) p).toList();
        BigDecimal prezzoTotal = BigDecimal.ZERO;
        for (ItemDto i : itemDtoList) {
            prezzoTotal = prezzoTotal . add(i.getPrezzoQta());
        }
        CarrelloDto carrelloDto = new CarrelloDto.Builder().itemListDto(itemDtoList).prezzoTotal(prezzoTotal).build();
        return carrelloDto;
    }
}
