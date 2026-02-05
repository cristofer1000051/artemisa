package com.andromeda.artemisa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.artemisa.entities.dtos.ClienteDto;
import com.andromeda.artemisa.entities.dtos.LoginDto;
import com.andromeda.artemisa.security.services.AuthService;
import com.andromeda.artemisa.security.utils.responses.AuthResponse;

@RestController
@RequestMapping("/auth")
public class AutenticazioneController {

    private final AuthService authService;

    public AutenticazioneController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> AuthLogIn(@RequestBody LoginDto loginDto) {
        String jwtToken = authService.authenticate(loginDto);
        if(loginDto.getCarrelloLocale()!=null && !loginDto.getCarrelloLocale().isEmpty()){
            
        }
        AuthResponse response = new AuthResponse();
        response.setSuccess(true);
        response.setMessage("Hai iniziato sessione con successo!");
        response.setToken(jwtToken);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/signInClient")
    public ResponseEntity<String> AuthSignIn(@RequestBody ClienteDto clientDto) {
        return ResponseEntity.ok("Prodotto aggiunto!");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> LogOut(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            //Rimuoviamo Bearer_
            String token = authHeader.substring(7);
            authService.logOut(token);
        }

        return ResponseEntity.badRequest().body("Token non valido");
    }

}
