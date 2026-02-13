package com.andromeda.artemisa.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.artemisa.entities.dtos.ItemInt.ItemDto;
import com.andromeda.artemisa.services.PagamentoService;
import com.andromeda.artemisa.utils.CarrelloValidationException;
import com.andromeda.artemisa.utils.PaymentResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PagamentoService pagamentoService;

    public PaymentController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent() {

        try {
            //TO DO
            List<ItemDto> itemDtoList = pagamentoService.verificareProdotti();
            // 1. Chiama il service che parla con Stripe
            PaymentResponse response = pagamentoService.createPaymentIntent(itemDtoList);
            // 2. Restituisce il codice "segreto" al frontend
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            List<String> errori = new ArrayList<>();
            if (e instanceof CarrelloValidationException c) {
                errori = c.getErrori();
            }
            errori.add("errore durante la creazione del pagamento");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errori);
        }
    }

    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String, String> payload) {
        String paymentIntentId = payload.get("paymentIntentId");

        if (paymentIntentId == null) {
            return ResponseEntity.badRequest().body("Manca il paymentIntentId");
        }

        try {
            // Chiamiamo il service per verificare se è VERO che ha pagato
            boolean esito = pagamentoService.verificaEConcludi(paymentIntentId);

            if (esito) {
                return ResponseEntity.ok("Pagamento verificato e Ordine creato!");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il pagamento non risulta completato.");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Errore verifica: " + e.getMessage());
        }
    }

}
