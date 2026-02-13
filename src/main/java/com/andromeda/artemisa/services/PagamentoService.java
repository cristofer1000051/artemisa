package com.andromeda.artemisa.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.andromeda.artemisa.entities.Prodotto;
import com.andromeda.artemisa.entities.dtos.ItemInt.ItemDto;
import com.andromeda.artemisa.entities.dtos.PaymentInfoDTO;
import com.andromeda.artemisa.entities.dtos.ProdottoDto;
import com.andromeda.artemisa.repositories.ProdottoRepository;
import com.andromeda.artemisa.utils.CarrelloValidationException;
import com.andromeda.artemisa.utils.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
public class PagamentoService {

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    private final RedisTemplate<String, Object> redisTemplate;
    private final ProdottoRepository prodottoRepository;
    private final CarrelloService carrelloService;

    public PagamentoService(RedisTemplate<String, Object> redisTemplate, ProdottoRepository prodottoRepository, CarrelloService carrelloService) {
        this.redisTemplate = redisTemplate;
        this.prodottoRepository = prodottoRepository;
        this.carrelloService = carrelloService;
    }

    public PaymentResponse createPaymentIntent(PaymentInfoDTO paymentInfo) {
        Stripe.apiKey = stripeApiKey;
        long amountInCents = paymentInfo.getAmount() * 100;
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("eur")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .build();
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return new PaymentResponse(paymentIntent.getClientSecret());
        } catch (Exception e) {
            throw new RuntimeException("Errore durante la creazione del pagamento", e);
        }
    }

    public List<ItemDto> verificareProdotti() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();

        //Abbiamo recuperato i dati dal carrello e gli abbiamo trasformato in una mappa per poter confrontare i dati tra Redis e il database
        Map<String, ItemDto> itemRedisMap = redisTemplate.opsForHash().entries(chiaveCarrello).entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> (String) e.getKey(),
                        e -> (ItemDto) e.getValue()
                ));

        Map<String, Prodotto> prodottiDb = prodottoRepository.findBycodProdottoIn(new ArrayList<>(itemRedisMap.keySet()))
                .stream()
                .collect(Collectors.toMap(p -> p.getCodProdotto(), p -> p));

        List<ItemDto> prodottiValidi = new ArrayList<>();
        List<String> errori = new ArrayList<>();
        for (Map.Entry<String, ItemDto> entry : itemRedisMap.entrySet()) {
            String codProdotto = entry.getKey();
            ItemDto itemDtoRedis = entry.getValue();

            if (!prodottiDb.containsKey(codProdotto)) {
                this.carrelloService.deleteById(codProdotto);
                errori.add("Il prodotto " + itemDtoRedis.getNome() + " nel carrello non esiste più!");
            }
            Prodotto prodottoDb = prodottiDb.get(codProdotto);
            BigDecimal pzProdtRedis = itemDtoRedis.getPrezzo();
            BigDecimal pzProdtDb = prodottoDb.getPrezzo();
            int quantita = itemDtoRedis.getQuantita();

            if (quantita > prodottoDb.getStock()) {
                errori.add("Il prodotto " + itemDtoRedis.getNome() + " nel carrello ha superato lo stock disponibile!");
            }
            if (pzProdtDb.compareTo(pzProdtRedis) != 0) {
                errori.add("Il prodotto " + itemDtoRedis.getNome() + " è stato variato il suo prezzo, il suo prezzo attuale è " + pzProdtDb + " invece di " + pzProdtRedis + "!");
            }
            ItemDto itemAggiornato = new ItemDto.Builder()
                    .codProdotto(prodottoDb.getCodProdotto())
                    .nome(prodottoDb.getNome())
                    .prezzo(pzProdtDb)
                    .quantita(itemDtoRedis.getQuantita())
                    .prezzoQta(pzProdtDb.multiply(BigDecimal.valueOf(quantita)))
                    .build();

            prodottiValidi.add(itemAggiornato);
        }
        if(!errori.isEmpty()){
            throw new CarrelloValidationException(errori);
        }
        return prodottiValidi;
    }
}
