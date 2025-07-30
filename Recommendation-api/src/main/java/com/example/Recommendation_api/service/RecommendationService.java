package com.example.Recommendation_api.service;

import com.example.Recommendation_api.model.Product;
import com.example.Recommendation_api.model.RecommendationResponse;
import com.example.Recommendation_api.model.UserBehaviorEvent;
import com.example.Recommendation_api.repository.UserBehaviorEventRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final ChatClient chatClient; // Spring AI
    private final RestTemplate restTemplate;
    private final UserBehaviorEventRepository userBehaviorEventRepository;




    @Value("${product.service.url:http://localhost:8081}") // file el properties kan 8081 lw 7d ghayer haga fel port yghayer da
    private String productServiceUrl;

    public void processBehavior(UserBehaviorEvent event) {
        userBehaviorEventRepository.save(event);
        log.info("Stored behavior for user: {}", event.getUserId());
    }

    public RecommendationResponse getRecommendations(String userId) {
        List<UserBehaviorEvent> events = userBehaviorEventRepository.findByUserId(userId);
        if (events.isEmpty()) {
            return new RecommendationResponse(List.of(), "No behavior found for user " + userId);
        }

        // Create prompt for Spring AI
        StringBuilder prompt = new StringBuilder();
        prompt.append("Recommend 3 relevant product IDs for a user who performed the following actions:\n");

        for (UserBehaviorEvent event : events) {
            prompt.append("- ")
                    .append(event.getEventType())
                    .append(" on product ")
                    .append(event.getProductId())
                    .append("\n");
        }

        prompt.append("Only return a JSON array of 3 product IDs, like: [\"prod1\", \"prod2\", \"prod3\"]");

        String aiResponse = chatClient.call(prompt.toString());
        log.info("AI response: {}", aiResponse);

        // Parse the AI output (very simple JSON parser for array of strings)
        List<String> recommendedProductIds = parseProductIds(aiResponse);

        // Fetch full product info
        List<Product> recommendedProducts = new ArrayList<>();
        for (String productId : recommendedProductIds) {
            try {
                ResponseEntity<Product> response = restTemplate.getForEntity(
                        productServiceUrl + "/products/" + productId,
                        Product.class
                );
                if (response.getStatusCode().is2xxSuccessful()) {
                    recommendedProducts.add(response.getBody());
                }
            } catch (Exception ex) {
                log.warn("Failed to fetch product {}", productId);
            }
        }

        return new RecommendationResponse(recommendedProducts, "Based on user behavior");
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private List<String> parseProductIds(String jsonArrayString) {
        try {
            return objectMapper.readValue(jsonArrayString, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            log.error("Failed to parse product IDs from AI response: {}", jsonArrayString, e);
            return List.of(); // return empty list on failure
        }
    }
}
