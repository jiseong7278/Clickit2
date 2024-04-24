package com.project.clickit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void setData(String key, Object data) {
        redisTemplate.opsForValue().set(key, data);
    }

    public void setData(String key, Object data, Duration duration){
        redisTemplate.opsForValue().set(key, data, duration);
    }

    @Transactional(readOnly = true)
    public String getData(String key){
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null) return null;
        return (String) values.get(key);
    }

    public void deleteData(String key){
        redisTemplate.delete(key);
    }

    public void expireData(String key, int timeout){
        redisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public void setHashOps(String key, Map<String, Object> data){
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        values.putAll(key, data);
    }

    @Transactional(readOnly = true)
    public String getHashOps(String key, String hashKey){
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        return Boolean.TRUE.equals(values.hasKey(key, hashKey)) ?
                (String) redisTemplate.opsForHash().get(key, hashKey) : null;
    }

    public void deleteHashOps(String key, String hashKey){
        HashOperations<String, Object, Object> values = redisTemplate.opsForHash();
        values.delete(key, hashKey);
    }
}
