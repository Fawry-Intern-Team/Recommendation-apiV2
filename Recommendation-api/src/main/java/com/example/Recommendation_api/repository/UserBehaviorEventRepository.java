package com.example.Recommendation_api.repository;

import com.example.Recommendation_api.model.UserBehaviorEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface UserBehaviorEventRepository extends JpaRepository<UserBehaviorEvent, UUID> {
    List<UserBehaviorEvent> findByUserId(String userId);
}
