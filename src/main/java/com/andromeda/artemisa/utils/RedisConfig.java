package com.andromeda.artemisa.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        //Strumento che useremo per inviare commandi a Redis
        RedisTemplate<String, Object> template = new  RedisTemplate<>();
        //Collega il template alla connessione attiva (Configurata automaticamente da Spring)
        template.setConnectionFactory(connectionFactory);
        
        //Imposta come vengono scritte le chiavi: usa testo semplice (String)
        template.setKeySerializer(new StringRedisSerializer());

        //Imposta come vengono scritti i valori: usa il formato JSON (leggibile da umani)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
}
