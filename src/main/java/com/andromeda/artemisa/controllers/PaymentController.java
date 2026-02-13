package com.andromeda.artemisa.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.artemisa.entities.dtos.ItemInt.ItemDto;
import com.andromeda.artemisa.entities.dtos.PaymentInfoDTO;
import com.andromeda.artemisa.services.PagamentoService;
import com.andromeda.artemisa.utils.CarrelloValidationException;
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

        try {
            //TO DO
            List<ItemDto> itemDtoList = pagamentoService.verificareProdotti();
            // 1. Chiama il service che parla con Stripe
            PaymentResponse response = pagamentoService.createPaymentIntent(paymentInfo);
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

}
