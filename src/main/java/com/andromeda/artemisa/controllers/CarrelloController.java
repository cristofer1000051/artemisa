package com.andromeda.artemisa.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.artemisa.entities.dtos.ProdottoDto;
import com.andromeda.artemisa.services.ProdottoService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RequestMapping("/cart")
@RestController
public class CarrelloController {

    private final ProdottoService prodottoService;

    public CarrelloController(ProdottoService prodottoService) {
        this.prodottoService = prodottoService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> aggCarrello(@RequestBody ProdottoDto prodottoDto) {

        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> rmProdCarrello(@RequestBody ProdottoDto prodottoDto) {

        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @PutMapping("/modify")
    public ResponseEntity<String> mdProdCarrello(@RequestBody Integer quantita) {
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @DeleteMapping("/removeAll")
    public ResponseEntity<String> rmCarrello(@RequestBody List<ProdottoDto> prodottoDto) {

        return ResponseEntity.ok("Prodotto aggiunto!");
    }

}
