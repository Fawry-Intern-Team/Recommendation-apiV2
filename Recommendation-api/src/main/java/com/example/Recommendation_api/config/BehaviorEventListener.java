package com.example.Recommendation_api.config;

import com.example.Recommendation_api.model.UserBehaviorEvent;
import com.example.Recommendation_api.service.RecommendationService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BehaviorEventListener {

    private final RecommendationService recommendationService;

    @RabbitListener(queues = RabbitMQConfig.USER_BEHAVIOR_QUEUE)
    public void handleUserBehavior(UserBehaviorEvent event) {
        try {
            log.info("Received behavior event: {}", event);
            recommendationService.processBehavior(event);
        } catch (Exception e) {
            log.error("Error processing behavior event: {}", event, e);
        }
    }
}
