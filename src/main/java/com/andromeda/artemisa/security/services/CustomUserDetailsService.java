package com.andromeda.artemisa.security.services;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.andromeda.artemisa.entities.Utente;
import com.andromeda.artemisa.security.entities.CustomUserDetails;
import com.andromeda.artemisa.security.repositories.AuthRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final AuthRepository authRepository;

    public CustomUserDetailsService(AuthRepository authRepository){
        this.authRepository = authRepository;
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
       Utente utente = authRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("L'utente non esiste"));
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+utente.getRol()));
       return new CustomUserDetails(utente.getEmail(),utente.getHashPassword(),authorities,utente.getId());
    }
}
