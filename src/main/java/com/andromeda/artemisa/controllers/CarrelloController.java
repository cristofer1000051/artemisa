package com.andromeda.artemisa.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.artemisa.entities.dtos.ProdottoDtoTemp;
import com.andromeda.artemisa.services.CarrelloService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RequestMapping("/cart")
@RestController
public class CarrelloController {

    private final CarrelloService carrelloService;

    public CarrelloController(CarrelloService carrelloService) {
        this.carrelloService = carrelloService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> aggCarrello(@RequestBody ProdottoDtoTemp prodDtoTemp) {
        this.carrelloService.save(prodDtoTemp);
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> rmProdCarrello(@RequestBody ProdottoDtoTemp prodDtoTemp) {

        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @PutMapping("/modify")
    public ResponseEntity<String> mdProdCarrello(@RequestBody ProdottoDtoTemp prodDtoTemp) {
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @DeleteMapping("/removeAll")
    public ResponseEntity<String> rmCarrello(@RequestBody List<String> keys) {

        return ResponseEntity.ok("Prodotto aggiunto!");
    }

}
