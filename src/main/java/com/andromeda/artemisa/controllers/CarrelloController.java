package com.andromeda.artemisa.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.artemisa.entities.dtos.CarrelloDto;
import com.andromeda.artemisa.entities.dtos.ItemInt.ItemCarDto;
import com.andromeda.artemisa.services.CarrelloService;



@RequestMapping("/cart")
@RestController
public class CarrelloController {

    private final CarrelloService carrelloService;

    public CarrelloController(CarrelloService carrelloService) {
        this.carrelloService = carrelloService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> aggCarrello(@RequestBody ItemCarDto itemCarDto) {
        this.carrelloService.save(itemCarDto);
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @DeleteMapping("/delete/{codProd}")
    public ResponseEntity<String> rmProdCarrello(@PathVariable String codProd) {
        this.carrelloService.deleteById(codProd);
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @DeleteMapping("/removeAll")
    public ResponseEntity<?> rmCarrello() {
        this.carrelloService.deleteAll();
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @GetMapping("/list")
    public ResponseEntity<CarrelloDto> listCarrello(){
        return ResponseEntity.ok(this.carrelloService.findAll());
    }
}
