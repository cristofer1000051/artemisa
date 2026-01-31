package com.andromeda.artemisa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.artemisa.entities.dtos.ClienteDto;
import com.andromeda.artemisa.entities.dtos.LoginDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/auth")
public class AutenticazioneController {

    public AutenticazioneController() {

    }

    @PostMapping("/login")
    public ResponseEntity<String> AuthLogIn(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok("Hai iniziato sesione con successo!");
    }

    @PostMapping("/signInClient")
    public ResponseEntity<String> AuthSignIn(@RequestBody ClienteDto clientDto) {
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

 
}
