package com.andromeda.artemisa.security.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.andromeda.artemisa.entities.Utente;
import com.andromeda.artemisa.entities.dtos.LoginDto;
import com.andromeda.artemisa.security.entities.CustomUserDetails;
import com.andromeda.artemisa.security.repositories.AuthRepository;
import com.andromeda.artemisa.utils.RedisService;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            AuthRepository authRepository,
            PasswordEncoder passwordEncoder,
            RedisService redisService
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
    }

    public void registrareUtente(Utente utente) {
        utente.generateHashPassword(passwordEncoder);
        authRepository.save(utente);
    }

    @Transactional
    public String authenticate(LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authToken
                = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
        Authentication auth = authenticationManager.authenticate(authToken);
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
        
        String jwtToken = jwtService.generateToken(userDetails.getUsername(), userDetails.getAuthorities(), userDetails.getId());

        redisService.save(jwtToken, userDetails.getUsername(), 30);


        return jwtToken;
    }

    public void logOut(String token) {
        redisService.cancella(token);

    }

}
