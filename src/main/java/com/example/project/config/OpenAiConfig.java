package com.example.project.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class OpenAiConfig {
   @Value("${spring.ai.openai.api-key}")
   private String apiKey;
}
