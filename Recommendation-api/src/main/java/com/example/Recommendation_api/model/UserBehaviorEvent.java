package com.example.Recommendation_api.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_behavior_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBehaviorEvent {
    @Id
    @GeneratedValue
    private UUID id;

    private String userId;
    private String eventType; // e.g., ADD_TO_CART, CHECKOUT
    private String productId;
    private LocalDateTime timestamp;
}

