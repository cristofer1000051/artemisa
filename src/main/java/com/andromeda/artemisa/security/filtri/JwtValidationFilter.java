package com.andromeda.artemisa.security.filtri;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.andromeda.artemisa.security.services.JwtService;
import static com.andromeda.artemisa.security.utils.config.TokenJwtConfig.CONTENT_TYPE;
import static com.andromeda.artemisa.security.utils.config.TokenJwtConfig.PREFIX_TOKEN;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.lang.Arrays;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtValidationFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    public JwtValidationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            filterChain.doFilter(request, response);
            return;
        }
        String token = header.replace(PREFIX_TOKEN, "");
        try {
            Claims claims = jwtService.extractClaims(token);
            Long utenteId = claims.get("utenteId", Long.class);
            String email = claims.getSubject();
            String authorities = claims.get("authorities", String.class);
            List<String> listAuthorities = new ArrayList<>(Arrays.asList(authorities.split(",")));
            Collection<? extends GrantedAuthority> grantedAuthorities = listAuthorities
                    .stream()
                    .map(sa -> new SimpleGrantedAuthority(sa))
                    .collect(Collectors.toList());
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(utenteId, null, grantedAuthorities);
            authenticationToken.setDetails(email);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            Map<String, String> body = new HashMap<>();
            body.put("error", e.getMessage());
            body.put("message", "Token is not valid!");
            String jsonBody = new ObjectMapper().writeValueAsString(body);
            response.getWriter().write(jsonBody);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(CONTENT_TYPE);
        }
    }

}
