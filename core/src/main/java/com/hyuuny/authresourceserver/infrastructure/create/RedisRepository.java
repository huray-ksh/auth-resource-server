package com.hyuuny.authresourceserver.infrastructure.create;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisRepository {

  private final RedisTemplate<String, Object> redisTemplate;

  public void set(String key, Object o, long minutes) {
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(o.getClass()));
    redisTemplate.opsForValue().set(key, o, minutes, TimeUnit.MINUTES);
  }

  public Object get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public boolean hasKey(String key) {
    return redisTemplate.hasKey(key);
  }

  public void clear() {
    log.debug("모든 캐시를 삭제합니다...");
    try {
      redisTemplate.execute((RedisCallback) connection -> {
        connection.flushAll();
        return null;
      });
    } catch (Exception e) {
      log.warn("모든 캐시를 삭제하는데 실패했습니다.", e);
    }
  }
}
