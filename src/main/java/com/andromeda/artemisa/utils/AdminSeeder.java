package com.andromeda.artemisa.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.andromeda.artemisa.entities.Amministratore;
import com.andromeda.artemisa.repositories.AmminRepository;

@Configuration
//1. PropertySource: "Dice carica le variabile da questo file"
//2. classpath: indica la cartella src/main/resources
//3. ignoreResourceNotFound = true evita errori se il file non esiste(es .in prod)
@PropertySource(value="classpath:recovery.properties",ignoreResourceNotFound=true)
public class AdminSeeder {
    //1. Aggiungiamo false come default se non si trova nulla
    @Value("${admin.recovery.enabled:false}")
    private boolean recoveryMode;
    @Value("${admin.recovery.email}")
    private String adminEmail;
    @Value("${admin.recovery.password}")
    private String adminPassword;

    @Bean
    CommandLineRunner initAdmin(AmminRepository repo, PasswordEncoder passwordEncoder) {
        return args -> {
            //1. Controlla se il "Modo recupero" è attivo
            if (!recoveryMode) {
                return;
            }
            System.out.println("MODALITA RECUPERO ATTIVA: Tentativo creazione admin...");
            //2. Controlla se esiste già un utente con questa mail
            if (repo.findByEmail(adminEmail).isPresent()) {
                System.out.println("L'admin" + adminEmail + "esiste già. Nessuna azione");
                return;
            }
            //3. Crea il nuevo admin
            Amministratore ammin = new Amministratore.AmminBuilder()
                    .name("master")
                    .cognome("master")
                    .email(adminEmail)
                    .password(adminPassword)
                    .build();
            ammin.generateHashPassword(passwordEncoder);
            repo.save(ammin);
            System.out.println("NUOVO AMMINISTRATORE CREATO CON SUCCESSO!" + adminEmail);
        };
    }
}
