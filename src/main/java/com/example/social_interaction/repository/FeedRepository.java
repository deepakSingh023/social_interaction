package com.example.social_interaction.repository;

import com.example.social_interaction.entity.Feed;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedRepository extends MongoRepository<Feed, String> {

    boolean existsByAuthorIdOrRecipientUserId(String authorId, String recipientUserId);
}
