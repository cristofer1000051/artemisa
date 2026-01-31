package com.andromeda.artemisa.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.andromeda.artemisa.entities.dtos.AdminDto;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @PostMapping("/signInAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> AuthSignIn(@RequestBody AdminDto AdminDto) {
        return ResponseEntity.ok("Prodotto aggiunto!");
    }
}
