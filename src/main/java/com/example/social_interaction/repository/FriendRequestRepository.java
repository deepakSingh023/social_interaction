package com.example.social_interaction.repository;

import com.example.social_interaction.entity.FriendRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {

    Page<FriendRequest> findByReceiverId(String receiverId, Pageable pageable);

    Page<FriendRequest> findBySenderId(String senderId, Pageable pageable);

    Optional<FriendRequest> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
            String sender1Id,
            String receiver1Id,
            String sender2Id,
            String receiver2Id
    );
}
