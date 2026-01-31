package com.andromeda.artemisa.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public RedisService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //1. Salvare un dato con scadenza!
    public void save(String chiave, String valore, long durataInMinuti) {
        redisTemplate.opsForValue().set(chiave, valore, durataInMinuti, TimeUnit.MINUTES);
    }

    //2.Leggere un dato
    public String leggi(String chiave) {
        return redisTemplate.opsForValue().get(chiave);
    }

    //3. Cancellare un dato
    public void cancella(String chiave) {
        redisTemplate.delete(chiave);
    }

    //4. Controllare se esiste
    public boolean esiste(String chiave) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(chiave));
    }
    //5. Metodo per allungare la vita alla sessione
    public void aggiornaScadenza(String token, long minuti){
        //se la chiave esiste, resetta il timer
        redisTemplate.expire(token,minuti,TimeUnit.MINUTES);
    }
}
