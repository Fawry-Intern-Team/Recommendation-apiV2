package com.example.Recommendation_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {
    @Bean
    public ChatClient chatClient(OpenAiChatClient openAiChatClient) {
        return openAiChatClient;
    }
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}