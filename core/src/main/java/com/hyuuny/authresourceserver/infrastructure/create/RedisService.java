package com.hyuuny.authresourceserver.infrastructure.create;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setObject(String key, Object serializableValue, Long milliSecond) {
        try {
            ValueOperations<String, Object> valueOperations = this.redisTemplate.opsForValue();
            if (!isEmpty(milliSecond)) {
                valueOperations.set(key, serializableValue, milliSecond, TimeUnit.MILLISECONDS);
            } else {
                valueOperations.set(key, serializableValue);
            }
        } catch (Exception e) {
            log.info("### Redis Error occurred. {}", e.getMessage());
        }
    }

    public void setObjectAfterForceKeyExpiration(String key, Object serializableValue, Long milliSecond) {
        forceKeyExpiration(key);
        setObject(key, serializableValue, milliSecond);
    }

    public Object getObject(String key) {
        try {
            ValueOperations<String, Object> valueOperations = this.redisTemplate.opsForValue();
            return valueOperations.get(key);
        } catch (Exception e) {
            log.info("### Redis Error occurred. {}", e.getMessage());
        }
        return null;
    }

    public void forceKeyExpiration(String key) {
        try {
            ValueOperations<String, Object> valueOperations = this.redisTemplate.opsForValue();
            valueOperations.set(key, "", 1, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.info("### Redis Error occurred. {}", e.getMessage());
        }
    }



}
