package com.andromeda.artemisa.security.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.andromeda.artemisa.entities.Utente;
import com.andromeda.artemisa.entities.dtos.LoginDto;
import com.andromeda.artemisa.security.repositories.AuthRepository;
import static com.andromeda.artemisa.security.utils.config.TokenJwtConfig.PREFIX_TOKEN;
import com.andromeda.artemisa.security.utils.responses.AuthResponse;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private AuthRepository authRepository;
    private PasswordEncoder passwordEncoder;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            AuthRepository authRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registrareUtente(Utente  utente) {
        utente.generateHashPassword(passwordEncoder);
        authRepository.save(utente);
    }

    public AuthResponse authenticate(LoginDto loginDto) {
        AuthResponse response = new AuthResponse();
        try {
            UsernamePasswordAuthenticationToken authToken
                    = new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword());
            Authentication auth = authenticationManager.authenticate(authToken);
            Utente utente = authRepository.findByEmail(auth.getName()).orElseThrow(() -> new UsernameNotFoundException("L'utente non esiste"));
            String jwtToken = jwtService.generateToken(auth.getName(), auth.getAuthorities(), utente.getId());
            response.setSuccess(true);
            response.setMessage("Has iniciado sesion correctamente");
            response.setToken(jwtToken);
        } catch (AuthenticationException e) {
            response.setSuccess(false);
            response.setMessage("Error de autenticación: " + e.getMessage());
        } catch (RuntimeException e) {
            response.setSuccess(false);
            response.setMessage("Un error interno a ocurrido: " + e.getMessage());
        }
        return response;
    }

    public void logOut(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith(PREFIX_TOKEN)) {
            return;
        }

    }

}
