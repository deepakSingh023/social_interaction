package com.example.social_interaction.repository;

import com.example.social_interaction.entity.Friends;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends MongoRepository<Friends,String> {


    Page<Friends> findBySenderIdOrReceiverId(
            String senderId,
            String receiverId,
            Pageable pageable
    );


    Optional<Friends> findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
            String sender1, String receiver1,
            String sender2, String receiver2
    );

    boolean existsBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
            String sender1, String receiver1,
            String sender2, String receiver2
    );

}
