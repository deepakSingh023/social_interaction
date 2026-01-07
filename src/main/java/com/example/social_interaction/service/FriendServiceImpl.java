package com.example.social_interaction.service;

import com.example.social_interaction.dto.friendRequest;
import com.example.social_interaction.entity.FollowRequest;
import com.example.social_interaction.entity.FriendRequest;
import com.example.social_interaction.entity.Friends;
import com.example.social_interaction.enums.FriendRequestStatus;
import com.example.social_interaction.repository.FriendRepository;
import com.example.social_interaction.repository.FriendRequestRepository;
import com.example.social_interaction.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FriendServiceImpl implements FriendService {

    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    // ---------------- ADD FRIEND ----------------

    @Override
    public void addFriend(String senderId, friendRequest request) {

        if (!userRepository.existsById(senderId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Sender not found");
        }

        if (!userRepository.existsById(request.getReceiverId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Receiver not found");
        }

        boolean alreadyFriends =
                friendRepository.existsBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                        senderId, request.getReceiverId(),
                        request.getReceiverId(), senderId
                );

        if (alreadyFriends) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Already friends");
        }

        Optional<FriendRequest> existingRequest =
                friendRequestRepository.findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(
                        senderId, request.getReceiverId(),
                        senderId,request.getReceiverId()
                );

        // Auto-accept if opposite request exists
        if (existingRequest.isPresent()) {
            FriendRequest req = existingRequest.get();

            Friends friend = Friends.builder()
                    .senderId(req.getSenderId())
                    .senderAvatar(request.getSenderAvatar())
                    .senderName(request.getSenderName())
                    .receiverId(req.getReceiverId())
                    .receiverAvatar(request.getReceiverAvatar())
                    .receiverName(request.getReceiverName())
                    .acceptedAt(Instant.now())
                    .build();

            friendRepository.save(friend);
            friendRequestRepository.delete(req);
            return;
        }

        FriendRequest createRequest = FriendRequest.builder()
                .senderId(request.getSenderId())
                .senderAvatar(request.getSenderAvatar())
                .senderName(request.getSenderName())
                .receiverId(request.getReceiverId())
                .receiverAvatar(request.getReceiverAvatar())
                .receiverName(request.getReceiverName())
                .receivedAt(new Date())
                .build();

        friendRequestRepository.save(createRequest);
    }

    // ---------------- REMOVE FRIEND ----------------

    @Override
    public void removeFriend(String senderId, String receiverId) {

        Friends friend = friendRepository
                .findBySenderIdAndReceiverIdOrSenderIdAndReceiverId(
                        senderId, receiverId,
                        receiverId, senderId
                )
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Not friends"));

        friendRepository.delete(friend);
    }

    // ---------------- GET FRIENDS (PAGINATED) ----------------

    @Override
    public Page<Friends> getFriends(String userId, Pageable pageable) {
        return friendRepository.findBySenderIdOrReceiverId(
                userId,
                userId,
                pageable
        );
    }

    // ---------------- ACCEPT REQUEST ----------------

    @Override
    public void acceptRequest(String requestId, String currentUserId) {

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Friend request not found"));

        if (!request.getReceiverId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed");
        }

        Friends friend = Friends.builder()
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .acceptedAt(Instant.now())
                .build();

        friendRepository.save(friend);
        friendRequestRepository.delete(request);
    }

    // ---------------- REJECT REQUEST ----------------

    @Override
    public void rejectRequest(String requestId, String currentUserId) {

        FriendRequest request = friendRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Friend request not found"));

        if (!request.getReceiverId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed");
        }

        friendRequestRepository.delete(request);
    }

    // ---------------- GET REQUESTS (PAGINATED) ----------------

    @Override
    public Page<FriendRequest> getRequests(String userId, Pageable pageable) {
        return friendRequestRepository.findByReceiverId(userId, pageable);
    }
}
