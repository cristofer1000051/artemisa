package com.andromeda.artemisa.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        // Strumento principale per inviare comandi a Redis
        RedisTemplate<String, Object> template = new RedisTemplate<>();

        // Collega il template alla connessione attiva (gestita da Spring)
        template.setConnectionFactory(connectionFactory);

        // Prepara il convertitore (serializer)
        // Trasforma automaticamente gli oggetti Java in formato JSON
        RedisSerializer<Object> serializer = RedisSerializer.json();

        // Imposta le regole di conversione:
        // Le chiavi restano testo semplice (String), i valori diventano JSON
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        // Applica le stesse regole anche per le strutture Hash
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        return template;
    }
}
