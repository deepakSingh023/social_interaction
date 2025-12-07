package com.example.social_interaction.service;

import com.example.social_interaction.entity.FriendRequest;
import com.example.social_interaction.entity.Friends;
import com.example.social_interaction.enums.FriendRequestStatus;
import com.example.social_interaction.repository.FriendRepository;
import com.example.social_interaction.repository.FriendRequestRepository;
import com.example.social_interaction.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FriendServiceImpl implements FriendService{

    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Override
    public void addFriend(String senderId , String receiverId){

        if(!userRepository.existById(senderId)){
            throw new IllegalStateException("user does not exist");
        }
        if(!userRepository.existById(receiverId)){
            throw new IllegalStateException("receiver does not exist");
        }

        boolean alreadyFriends = friendRepository.existsBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                senderId, receiverId, receiverId, senderId
        );

        if(alreadyFriends){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "user already friends");
        }

        Optional<FriendRequest> existingRequest = friendRequestRepository.
                findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(senderId, receiverId, senderId, receiverId);


        if (existingRequest.isPresent()){
            FriendRequest req = existingRequest.get();

            Friends friend = Friends.builder()
                    .senderId(req.getSenderId())
                    .receiverId(req.getReceiverId())
                    .acceptedAt(new Date())
                    .build();

            friendRepository.save(friend);


            friendRequestRepository.delete(req);

            return;
        }

        FriendRequest request= FriendRequest.builder()
                .senderId(senderId)
                .receiverId(receiverId)
                .receivedAt(new Date())
                .status(FriendRequestStatus.PENDING)
                .build();

        friendRequestRepository.save(request);

    }


    @Override
    public void removeFriend(String senderId, String receiverId) {

        Optional<Friends> friend =
                friendRepository.findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                        senderId, receiverId,
                        receiverId, senderId
                );

        if (friend.isEmpty()) {
            throw new IllegalStateException("Not friends");
        }

        friendRepository.delete(friend.get());
    }

    @Override
    public List<Friends> getFriends(String userId){

       return  friendRepository.findBySenderIdOrReceiverId(userId,userId);
    }



}
