package com.andromeda.artemisa.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.andromeda.artemisa.entities.Prodotto;
import com.andromeda.artemisa.entities.dtos.ItemInt.ItemDto;
import com.andromeda.artemisa.repositories.ProdottoRepository;
import com.andromeda.artemisa.utils.CarrelloValidationException;
import com.andromeda.artemisa.utils.Function;
import com.andromeda.artemisa.utils.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
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

    public PaymentResponse createPaymentIntent(List<ItemDto> prodottiVerificati) throws StripeException {
        //1.- CALCOLO SICURO DEL TOTALE
        BigDecimal totale = BigDecimal.ZERO;
        for (ItemDto item : prodottiVerificati) {
            totale = totale.add(item.getPrezzoQta());
        }
        //2. CONVERSIONE IN CENTESIMI (Es. 10.50 -> 1050 centesimi)
        //Stripe accetta solo Long (interi)
        Long amountInCents = totale.multiply(new BigDecimal(100)).longValue();
        Stripe.apiKey = stripeApiKey;

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountInCents)
                .setCurrency("eur")
                .setReceiptEmail(Function.authentication().getUsername())
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true)
                                .build()
                )
                .putMetadata("email", Function.authentication().getUsername())
                .build();
        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return new PaymentResponse(paymentIntent.getClientSecret());

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
        if (!errori.isEmpty()) {
            throw new CarrelloValidationException(errori);
        }
        return prodottiValidi;
    }

    public boolean verificaEConcludi(String paymentIntentId) throws Exception {
        //1. CHIEDI A STRIPE(SERVER TO SERVER)
        PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

        //2. CONTROLLA LO STATO
        // Gli stati buoni sono: "succeeded" (pagato) o "processing" (in corso, ma sicuro)
        String stato = paymentIntent.getStatus();
        if ("succeeded".equals(stato)) {

            // --- QUI FAI LE TUE OPERAZIONI ---
            // A. Recupera l'utente dai metadata (se li avevi messi) o dal Context
            String username = paymentIntent.getMetadata().get("email");
            System.out.println("L'utente è: " + username);
            // Oppure usa SecurityContextHolder se l'utente è loggato

            System.out.println("Soldi incassati! Importo: " + paymentIntent.getAmount());
            // B. Svuota il carrello Redis
            carrelloService.deleteAll();
            // C. Salva l'ordine nel DB
            // ordineRepository.save(...);
            return true; // Tutto OK
        }
        // Se lo stato è "requires_payment_method" o "canceled", il pagamento è fallito
        System.out.println("Pagamento NON valido. Stato: " + stato);
        return false;
    }
}
