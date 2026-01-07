package com.example.social_interaction.service;
import com.example.social_interaction.dto.followRequest;
import com.example.social_interaction.entity.FollowRequest;
import com.example.social_interaction.entity.Follower;
import com.example.social_interaction.repository.FollowRequestRepository;
import com.example.social_interaction.repository.RelationRepository;
import com.example.social_interaction.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.LocalTime.now;

@Service
@AllArgsConstructor
public class FollowService implements RelationService{


  private final RelationRepository relationRepository;

  private final UserRepository userRepository;

  private final FollowRequestRepository followRequestRepository;

    @Override
    public void followRequest(String userId, followRequest request) {

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("Sender user does not exist");
        }

        if (!userRepository.existsById(request.getFollowedId())) {
            throw new IllegalArgumentException("Receiver user does not exist");
        }

        if (request.getFollowedId().equals(userId)) {
            throw new IllegalArgumentException("You cannot send a follow request to yourself");
        }

        // Already following
        if (relationRepository.existsByUserIdAndFollowedId(userId, request.getFollowedId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Already following");
        }

        // 🔒 Private account → create follow request
        if (Boolean.TRUE.equals(request.getPrvAcc())) {

            if (followRequestRepository
                    .existsByUserIdAndFollowedId(userId, request.getFollowedId())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Follow request already sent");
            }

            FollowRequest createRequest = FollowRequest.builder()
                    .userId(userId)
                    .userAvatar(request.getUserName())
                    .userName(request.getUserName())
                    .followedId(request.getFollowedId())
                    .followedAvatar(request.getFollowedAvatar())
                    .followedName(request.getFollowedName())
                    .createdAt(Instant.now())
                    .build();

            followRequestRepository.save(createRequest);
            return;
        }


        Follower follower = Follower.builder()
                .userId(userId)
                .userAvatar(request.getUserName())
                .userName(request.getUserName())
                .followedId(request.getFollowedId())
                .followedAvatar(request.getFollowedAvatar())
                .followedName(request.getFollowedName())
                .createdAt(new Date())
                .build();

        relationRepository.save(follower);
    }

    @Override
    public void acceptFollowRequest(String requestId) {

        FollowRequest request = followRequestRepository.findById(requestId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Follow request not found"));

        String userId = request.getUserId();        // sender
        String followedId = request.getFollowedId(); // receiver (me)

        // Prevent duplicate followers
        if (relationRepository.existsByUserIdAndFollowedId(userId, followedId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Already following");
        }

        // Create follower relationship
        Follower follower = Follower.builder()
                .userId(userId)
                .userAvatar(request.getUserAvatar())
                .userName(request.getUserName())
                .followedId(followedId)
                .followedAvatar(request.getFollowedAvatar())
                .followedName(request.getFollowedName())
                .createdAt(new Date())
                .build();

        relationRepository.save(follower);

        // Delete follow request after acceptance
        followRequestRepository.deleteById(requestId);
    }


    @Override
    public void rejectFollowRequest(String requestId) {

        if (!followRequestRepository.existsById(requestId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Follow request not found");
        }

        followRequestRepository.deleteById(requestId);
    }

    @Override
    public void stopFollowing(String userId, String followedId) {
        Follower follower = relationRepository
                .findByUserIdAndFollowedId(userId, followedId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No follow found"));

        relationRepository.delete(follower);
    }

    @Override
    public void removeFollower(String followedById, String userId) {
        Follower follower = relationRepository
                .findByUserIdAndFollowedId(followedById, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No follow found"));

        relationRepository.delete(follower);
    }


    @Override
    public Page<FollowRequest> getFollowRequests(String userId , Pageable pageable){
    return followRequestRepository.findByUserId(userId , pageable);

    }

    @Override
    public Page<Follower> getFollowing(String userId, Pageable pageable) {
        return relationRepository.findByUserId(userId, pageable);
    }

    @Override
    public Page<Follower> getFollowers(String userId, Pageable pageable) {
        return relationRepository.findByFollowedId(userId, pageable);
    }

}


