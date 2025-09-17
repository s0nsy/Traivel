package com.example.project.config;

import io.lettuce.core.ReadFrom;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;

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
   @Bean
   public LettuceClientConfiguration lettuceClientConfiguration(){
      return LettuceClientConfiguration.builder()
            .readFrom(ReadFrom.MASTER)
            .build();
   }
}
