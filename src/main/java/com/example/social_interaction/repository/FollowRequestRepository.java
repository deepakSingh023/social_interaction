package com.example.social_interaction.repository;

import com.example.social_interaction.entity.FollowRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FollowRequestRepository
        extends MongoRepository<FollowRequest, String> {

    boolean existsByUserIdAndFollowedId(String userId, String followedId);
}
