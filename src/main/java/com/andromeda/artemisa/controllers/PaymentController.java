package com.andromeda.artemisa.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.artemisa.entities.dtos.PaymentInfoDTO;
import com.andromeda.artemisa.services.PagamentoService;
import com.andromeda.artemisa.utils.PaymentResponse;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PagamentoService pagamentoService;

    public PaymentController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentInfoDTO paymentInfo) {
        List<String> errori = pagamentoService.verificareProdotti();
        if (!errori.isEmpty()) {
            // Se ci sono errori, blocchi tutto e li mostri all'utente
            return ResponseEntity.badRequest().body(errori);
        }
        try {
            // 1. Chiama il service che parla con Stripe
            PaymentResponse response = pagamentoService.createPaymentIntent(paymentInfo);
            // 2. Restituisce il codice "segreto" al frontend
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("errore durante la creazione del pagamento" + e.getMessage());
        }
    }

}
