package com.example.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;

@Configuration
public class RedisConfig {
   @Bean
   public RedisCacheConfiguration configuration(){
      return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(30))
            .disableCachingNullValues();
   }

   @Bean
   public RedisCacheManager cacheManager(RedisConnectionFactory factory){
      return RedisCacheManager.builder(factory)
            .cacheDefaults(configuration())
            .build();

   }
}
