package com.example.project.controller;

import com.example.project.kafka.DestinationConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/destinations")
public class PopularDestinationController {

    private final StringRedisTemplate redisTemplate;

    @GetMapping("/popular")
    public List<String> getPopular() {
        Set<ZSetOperations.TypedTuple<String>> result =
                redisTemplate.opsForZSet().reverseRangeWithScores(DestinationConsumer.REDIS_KEY, 0, 9);
        if (result == null) return List.of();
        return result.stream()
                .map(ZSetOperations.TypedTuple::getValue)
                .toList();
    }
}
