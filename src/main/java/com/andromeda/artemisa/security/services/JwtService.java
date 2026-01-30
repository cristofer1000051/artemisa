package com.andromeda.artemisa.security.services;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import static com.andromeda.artemisa.security.utils.config.TokenJwtConfig.SECRET_KEY;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtService {

    public String generateToken(String email, Collection<? extends GrantedAuthority> authorities, Long utenteId) {
        String roles = authorities
                .stream()
                .map(ga -> ga.getAuthority())
                .collect(Collectors.joining(","));

        Claims claims = Jwts.claims()
                .add("authorities", roles)
                .add("email", email)
                .add("utenteId", utenteId)
                .build();

        return createToken(claims, email);
    }

    public String createToken(Map<String, Object> claims, String email) {
        Date dateExpiration = new Date(System.currentTimeMillis() + 1000 * 60 * 1);
        String jwts = Jwts.builder()
                .claims(claims)
                .subject(email)
                .signWith(SECRET_KEY)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(dateExpiration)
                .compact();
        return jwts;
    }

    public Claims extractClaims(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
    }
}
