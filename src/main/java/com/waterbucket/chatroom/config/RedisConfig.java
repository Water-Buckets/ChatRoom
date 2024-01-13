package com.waterbucket.chatroom.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisConfig {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60))
                .disableCachingNullValues()
                .serializeKeysWith(keyPair())
                .serializeValuesWith(valuePair());
        return RedisCacheManager.builder(factory)
                .withCacheConfiguration("chatroom", cacheConfig).build();
    }

    private RedisSerializationContext.SerializationPair<String> keyPair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer());
    }

    private RedisSerializationContext.SerializationPair<Object> valuePair() {
        return RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());
    }

}