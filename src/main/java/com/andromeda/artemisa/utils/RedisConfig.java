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
        //Strumento che useremo per inviare commandi a Redis
        RedisTemplate<String, Object> template = new  RedisTemplate<>();
        //Collega il template alla connessione attiva (Configurata automaticamente da Spring)
        template.setConnectionFactory(connectionFactory);
        
        // 1. Usa il metodo factory 'json()'
        // Questo gestisce automaticamente la serializzazione degli oggetti generici
        RedisSerializer<Object> serializer = RedisSerializer.json();
        
        // 2. Imposta i serializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        return template;
    }
}
