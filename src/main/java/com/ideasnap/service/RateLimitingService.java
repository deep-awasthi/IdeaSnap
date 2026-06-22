package com.ideasnap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimitingService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${app.rate-limit.requests-per-minute:60}")
    private int limit;

    public boolean isAllowed(String clientKey) {
        long currentMinute = Instant.now().getEpochSecond() / 60;
        String redisKey = "ratelimit:" + clientKey + ":" + currentMinute;

        Long count = stringRedisTemplate.opsForValue().increment(redisKey);
        
        if (count != null && count == 1) {
            // Set TTL for the key to clear up memory in Redis
            stringRedisTemplate.expire(redisKey, 60, TimeUnit.SECONDS);
        }

        return count != null && count <= limit;
    }
}
