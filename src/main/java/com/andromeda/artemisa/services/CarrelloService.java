package com.andromeda.artemisa.services;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andromeda.artemisa.entities.dtos.CarrelloDto;
import com.andromeda.artemisa.entities.dtos.ItemInt.ItemCarDto;
import com.andromeda.artemisa.entities.dtos.ItemInt.ItemDto;
import com.andromeda.artemisa.entities.dtos.ProdottoDto;
import com.andromeda.artemisa.repositories.ProdottoRepository;

@Service
public class CarrelloService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final long MAX_ITEMS_PER_CART = 50;
    private final ProdottoRepository prodottoRepository;

    public CarrelloService(RedisTemplate<String, Object> redisTemplate, ProdottoRepository prodottoRepository) {
        this.redisTemplate = redisTemplate;
        this.prodottoRepository = prodottoRepository;
    }

    /**
     * Questa funzione consente aggiungere un nuovo prodotto nel carrello Se la
     * quantità di prodotti è superiore a la quantità impostata in
     * MAX_ITEMS_PER_CART, allora si interrompe la funzione. La funzione
     * permette verificare se essistono prodotti nel carrello, se esistono
     * allora la quantita del prodotto viene sommata e per conseguenza anche il
     * prezzo per quantità verrà incrementata. La funzione verifica la
     * correttezza dei dati confrontando i dati del carrello con quelli del
     * database.
     *
     * @param itemCar serve a passare un item dal frontend per aggiungerlo al
     * carrello.
     */
    @Transactional
    public void save(ItemCarDto itemCar) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();
        String codProd = itemCar.getCodProdotto();
        Long prodottiTotali = redisTemplate.opsForHash().size(chiaveCarrello);
        ItemDto itemNelCarrello = (ItemDto) redisTemplate.opsForHash().get(chiaveCarrello, codProd);
        ProdottoDto prodottoDto = prodottoRepository.findByCodProdotto(codProd).stream().map(p -> {
            ProdottoDto pDto = new ProdottoDto.Builder()
                    .codProdotto(p.getCodProdotto())
                    .nome(p.getNome())
                    .prezzo(p.getPrezzo())
                    .stock(p.getStock())
                    .build();
            return pDto;
        }).findFirst().orElse(null);

        Integer nuovaQuantita;

        if (prodottoDto == null || itemCar.getQuantita() == 0) {
            return;
        }
        if (itemNelCarrello != null) {
            nuovaQuantita = itemNelCarrello.getQuantita() + itemCar.getQuantita();

        } else {
            if (prodottiTotali >= MAX_ITEMS_PER_CART) {
                return;
            }
            nuovaQuantita = itemCar.getQuantita();
        }
        if (nuovaQuantita > prodottoDto.getStock()) {
            return;
        }
        ItemDto item = new ItemDto.Builder()
                .codProdotto(prodottoDto.getCodProdotto())
                .nome(prodottoDto.getNome())
                .prezzo(prodottoDto.getPrezzo())
                .quantita(nuovaQuantita)
                .prezzoQta(prodottoDto.getPrezzo().multiply(BigDecimal.valueOf(nuovaQuantita)))
                .build();

        redisTemplate.opsForHash().put(chiaveCarrello, codProd, item);

        redisTemplate.expire(chiaveCarrello, Duration.ofDays(1));
    }

    public void saveAll(List<ItemCarDto> itemCarDtoList) {
        /**
         * Itereremmo la lista @itemCarDtoList, per eliminare oggetti con il codice prodotto ripetuto
         * Inoltre verrano sommati le quantita e salvati in una mappa
         */
        Map<String, Integer> itemCarDtoUnique = new HashMap<>();
        for (ItemCarDto itemCarDto : itemCarDtoList) {
            itemCarDtoUnique.merge(itemCarDto.getCodProdotto(), itemCarDto.getQuantita(), Integer::sum);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();
        List<String> codProds = new ArrayList<>(itemCarDtoUnique.keySet());

        //recuperiamo i dati da Redis come una mappa 
        Map<String, ItemDto> itemNelCarrello = redisTemplate.opsForHash().entries(chiaveCarrello).entrySet().stream().collect(Collectors.toMap(
                e -> (String) e.getKey(),
                e -> (ItemDto) e.getValue()
        ));
        //Recuperiamo i dati dal database come una mappa per facilitare la ricerca dei prodotti
        Map<String, ProdottoDto> prodottoDtoListDB = prodottoRepository.findBycodProdottoIn(codProds).stream().map(p -> {
            ProdottoDto pDto = new ProdottoDto.Builder()
                    .codProdotto(p.getCodProdotto())
                    .nome(p.getNome())
                    .prezzo(p.getPrezzo())
                    .stock(p.getStock())
                    .build();
            return pDto;
        }).collect(Collectors.toMap(
                ProdottoDto::getCodProdotto,
                p -> p
        ));
        /**
         * #itemMap: conterrà gli itemDto che verrano salvati all'interno di Redis
         */
        Map<String, ItemDto> itemMap = new HashMap<>();
        int nuoviProdottiCounter = 0;
        //Itereremmo i dati lavorati non ripetuti passati dal frontend 
        for (Map.Entry<String, Integer> entry : itemCarDtoUnique.entrySet()) {
            String codProd = entry.getKey();
            int quantita = entry.getValue();
            ProdottoDto prodottoDb = prodottoDtoListDB.get(codProd);
            if (prodottoDb == null) {
                continue;
            }
            ItemDto itemEsistente = itemNelCarrello.get(codProd);
            int nuovaQuantitaTotale;
            if (itemEsistente != null) {
                // CASO 1 : Se l'item essiste già, allora aggiorniamo la quantita totale
                nuovaQuantitaTotale = itemEsistente.getQuantita() + quantita;
            } else {
                // CASO 2: Se l'item non esistesse allora , verrà assegnato il valore ricevuto dal frontend
                nuovaQuantitaTotale = quantita;
                nuoviProdottiCounter++;
            }
            //
            if (nuovaQuantitaTotale > prodottoDb.getStock()) {
                throw new RuntimeException("Stock insufficiente per il prodotto: " + codProd);
            }
            ItemDto itemFinale = new ItemDto.Builder()
                    .codProdotto(prodottoDb.getCodProdotto())
                    .nome(prodottoDb.getNome())
                    .quantita(nuovaQuantitaTotale)
                    .prezzo(prodottoDb.getPrezzo())
                    .prezzoQta(prodottoDb.getPrezzo().multiply(BigDecimal.valueOf(nuovaQuantitaTotale)))
                    .build();
            itemMap.put(prodottoDb.getCodProdotto(), itemFinale);
        }
        long prodottiTotali = itemNelCarrello.size() + nuoviProdottiCounter;
        if (prodottiTotali >= 50) {
            throw new RuntimeException("Limite carrello superato!");
        }
        if (!itemMap.isEmpty()) {
            redisTemplate.opsForHash().putAll(chiaveCarrello, itemMap);
            redisTemplate.expire(chiaveCarrello, Duration.ofDays(1));
        }
    }

    public void deleteById(String codProd) {
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
            prezzoTotal = prezzoTotal.add(i.getPrezzoQta());
        }
        CarrelloDto carrelloDto = new CarrelloDto.Builder().itemListDto(itemDtoList).prezzoTotal(prezzoTotal).build();
        return carrelloDto;
    }
}
