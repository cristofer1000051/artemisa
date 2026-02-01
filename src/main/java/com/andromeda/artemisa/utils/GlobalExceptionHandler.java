package com.andromeda.artemisa.utils;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.andromeda.artemisa.utils.dtos.ErrorDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex, WebRequest request) {
        //Construiamo il DTO

        ErrorDto errore = new ErrorDto.Builder()
                .messaggio("Si è verificato un errore interno imprevisto" + ex.getMessage())
                .codiceStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .path(request.getDescription(false))
                .build();

        return new ResponseEntity<>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleLoginFallito(BadCredentialsException ex, WebRequest request) {
        ErrorDto errore = new ErrorDto.Builder()
                .messaggio("Email o password errati. Riprova")
                .codiceStatus(HttpStatus.UNAUTHORIZED.value())
                .path(request.getDescription(false))
                .build();
        return new ResponseEntity<>(errore, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleConflict(DataIntegrityViolationException e){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Errore: Uno dei prodoti nel file esiste già (nome duplicato)");
    }
}
