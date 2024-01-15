package fpt.gymmanagement.service.impl;

import fpt.gymmanagement.service.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CacheServiceImpl implements CacheService {
    private final RedisTemplate<String, String> redisTemplate;

    public CacheServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void save(String key, String value, long timeLife) {
        redisTemplate.opsForValue().set(key, value, timeLife, TimeUnit.SECONDS);
        log.info("Save new cache with key: " + key);
    }

    @Override
    public void invalidCache(String key) {
        redisTemplate.delete(key);
        log.info("Remove data with key: " + key);
    }

    @Override
    public String getByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
