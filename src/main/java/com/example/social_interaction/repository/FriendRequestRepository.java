package com.example.social_interaction.repository;

import com.example.social_interaction.entity.FriendRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends MongoRepository<FriendRequest,String> {

    List<FriendRequest> findBySenderId(String senderId);

    List<FriendRequest> findByReceiverId(String receiverId);

    Optional<FriendRequest> findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(String sender1Id , String receiver1Id, String sender2Id , String receiver2Id);
}
