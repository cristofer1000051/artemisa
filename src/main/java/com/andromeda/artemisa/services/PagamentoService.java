package com.andromeda.artemisa.services;

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
import com.andromeda.artemisa.entities.dtos.PaymentInfoDTO;
import com.andromeda.artemisa.entities.dtos.ProdottoDto;
import com.andromeda.artemisa.repositories.ProdottoRepository;
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

    public List<String> verificareProdotti() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String chiaveCarrello = "cart:" + authentication.getName();
        List<Object> itemObj = redisTemplate.opsForHash().values(chiaveCarrello);

        List<ProdottoDto> itemDtoList = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        for (Object o : itemObj) {
            itemDtoList.add((ProdottoDto) o);
            ids.add((((ProdottoDto) o).getId()));
        }

        Map<Long, Prodotto> mappaProdotti = prodottoRepository.findByIdIn(ids)
                .stream()
                .collect(Collectors.toMap(p -> p.getId(), p -> p));

        List<String> errori = new ArrayList<>();
        List<ProdottoDto> prodottiValidi = new ArrayList<>();
        for (ProdottoDto itemRedis : itemDtoList) {
            Prodotto prodottoReale = mappaProdotti.get(itemRedis.getId());
            if (prodottoReale == null) {
                errori.add("Il prodotto " + itemRedis.getNome() + " non esiste più.");
                continue;
            }
            if (prodottoReale.getStock() <= 0 || prodottoReale.getStock() < itemRedis.getQuantita()) {
                errori.add("Non c'è abbastanza stock per " + itemRedis.getNome());
                continue;
            }
            if (prodottoReale.getPrezzo().compareTo(itemRedis.getPrezzo()) != 0) {
                itemRedis.setPrezzo(prodottoReale.getPrezzo());
                errori.add("Il prezzo di " + itemRedis.getNome() + " è cambiato.");
            }

            prodottiValidi.add(itemRedis);
        }

        if (!errori.isEmpty()) {
            this.carrelloService.deleteAll(); // Svuota il vecchio carrello sporco

            // Salva solo quelli validi
            for (ProdottoDto pdto : prodottiValidi) {
                this.carrelloService.save(pdto);
            }
        }

        return errori;
    }
}
