package com.hyuuny.authresourceserver.common.config;

import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisProperties.Cluster cluster = this.redisProperties.getCluster();

        GenericObjectPoolConfig<LettucePoolingClientConfiguration> poolConfig = new GenericObjectPoolConfig<>();
        RedisProperties.Lettuce lettuce = this.redisProperties.getLettuce();
        if (Optional.ofNullable(lettuce.getPool()).isPresent()) {
            RedisProperties.Pool lettucePool = lettuce.getPool();
            Optional.of(lettucePool.getMaxActive()).ifPresent(it -> poolConfig.setMaxTotal(lettucePool.getMaxActive()));
            Optional.of(lettucePool.getMaxIdle()).ifPresent(it -> poolConfig.setMaxIdle(lettucePool.getMaxIdle()));
            Optional.of(lettucePool.getMinIdle()).ifPresent(it -> poolConfig.setMinIdle(lettucePool.getMinIdle()));
        }
        LettucePoolingClientConfiguration lettucePoolingClientConfiguration = LettucePoolingClientConfiguration
                .builder()
                .poolConfig(poolConfig).build();

        RedisConfiguration redisConfiguration;
        if (cluster != null && !CollectionUtils.isEmpty(cluster.getNodes())) {
            redisConfiguration = new RedisClusterConfiguration(cluster.getNodes());
            if (Optional.ofNullable(this.redisProperties.getUsername()).isPresent()) {
                ((RedisClusterConfiguration) redisConfiguration).setUsername(this.redisProperties.getUsername());
            }
            if (Optional.ofNullable(this.redisProperties.getPassword()).isPresent()) {
                ((RedisClusterConfiguration) redisConfiguration).setPassword(this.redisProperties.getPassword());
            }
            if (Optional.ofNullable(cluster.getMaxRedirects()).isPresent()) {
                ((RedisClusterConfiguration) redisConfiguration).setMaxRedirects(cluster.getMaxRedirects());
            }
        } else {
            redisConfiguration = new RedisStandaloneConfiguration(
                    this.redisProperties.getHost(), this.redisProperties.getPort());
            if (Optional.ofNullable(this.redisProperties.getUsername()).isPresent()) {
                ((RedisStandaloneConfiguration) redisConfiguration).setUsername(this.redisProperties.getUsername());
            }
            if (Optional.ofNullable(this.redisProperties.getPassword()).isPresent()) {
                ((RedisStandaloneConfiguration) redisConfiguration).setPassword(this.redisProperties.getPassword());
            }
        }
        return (lettuce.getPool() != null && lettuce.getPool().getEnabled()) ?
                new LettuceConnectionFactory(redisConfiguration, lettucePoolingClientConfiguration)
                : new LettuceConnectionFactory(redisConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.java());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setHashValueSerializer(RedisSerializer.java());
        redisTemplate.setEnableTransactionSupport(true);
        return redisTemplate;
    }

}
