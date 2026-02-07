package com.andromeda.artemisa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.artemisa.entities.dtos.ItemDto;
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
    public ResponseEntity<String> aggCarrello(@RequestBody ItemDto itemDto) {
        this.carrelloService.save(itemDto);
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> rmProdCarrello(@RequestBody Long id) {
        this.carrelloService.deleteById(id);
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @DeleteMapping("/removeAll")
    public ResponseEntity<String> rmCarrello() {
        this.carrelloService.deleteAll();
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

}
