package com.example.social_interaction.service;
import com.example.social_interaction.dto.InteractionDto;
import com.example.social_interaction.dto.ProfileDto;
import com.example.social_interaction.dto.UpdateCounter;
import com.example.social_interaction.dto.followRequest;
import com.example.social_interaction.entity.FollowRequest;
import com.example.social_interaction.entity.Follower;
import com.example.social_interaction.enums.CounterType;
import com.example.social_interaction.repository.FollowRequestRepository;
import com.example.social_interaction.repository.RelationRepository;
import com.example.social_interaction.tasks.CounterClient;
import com.example.social_interaction.tasks.PostClient;
import com.example.social_interaction.tasks.ProfileClient;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;

import static java.time.LocalTime.now;

@Service
@RequiredArgsConstructor
public class FollowService implements RelationService{


  private final RelationRepository relationRepository;

  private final ProfileClient profileClient;

  private final FollowRequestRepository followRequestRepository;

  private final InteractonService interactonService;

  private final CounterClient counterClient;

  private final PostClient postClient;

  private final DenormalizeAndFeedService denormalizeAndFeedService;

  @Value("${service.secret}")
  private String secret;

    @Override
    public void followRequest(String userId, followRequest request) {

        Map<String, ProfileDto> profiles =
                profileClient.getProfiles(List.of(userId, request.getFollowedId()),secret);

        if (!profiles.containsKey(userId) ||
                !profiles.containsKey(request.getFollowedId())) {

            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "User not found"
            );
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




        if (Boolean.TRUE.equals(request.getPrvAcc())) {

            if (followRequestRepository
                    .existsByUserIdAndFollowedId(userId, request.getFollowedId())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Follow request already sent");
            }

            FollowRequest createRequest = FollowRequest.builder()
                    .userId(userId)
                    .userAvatar(profiles.get(userId).avatar())
                    .userName(profiles.get(userId).username())
                    .followedId(request.getFollowedId())
                    .followedAvatar(profiles.get(request.getFollowedId()).avatar())
                    .followedName(profiles.get(request.getFollowedId()).username())
                    .createdAt(Instant.now())
                    .build();

            followRequestRepository.save(createRequest);
            return;
        }


        Follower follower = Follower.builder()
                .userId(userId)
                .userAvatar(profiles.get(userId).avatar())
                .userName(profiles.get(userId).username())
                .followedId(request.getFollowedId())
                .followedAvatar(profiles.get(request.getFollowedId()).avatar())
                .followedName(profiles.get(request.getFollowedId()).username())
                .createdAt(Instant.now())
                .build();

        relationRepository.save(follower);

        interactonService.createInteraction(follower.getFollowedId(),follower.getUserId());
        

        denormalizeAndFeedService.followerWorker(new UpdateCounter(
                userId,
                CounterType.FOLLOWING,
                1
        ),new UpdateCounter(
                request.getFollowedId(),
                CounterType.FOLLOWER,
                1
        ),new InteractionDto(
                follower.getFollowedId(),follower.getUserId()
        ));

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
                .createdAt(Instant.now())
                .build();

        relationRepository.save(follower);

        UpdateCounter data = new UpdateCounter(
                request.getUserId(),
                CounterType.FOLLOWING,
                1
        );

        counterClient.denormalize(data,secret);

        UpdateCounter data2 = new UpdateCounter(
                request.getFollowedId(),
                CounterType.FOLLOWER,
                1
        );

        counterClient.denormalize(data2,secret);


        InteractionDto data3 = new InteractionDto(
                followedId,userId
        );

        postClient.createFeed(data3,secret);

        interactonService.createInteraction(followedId,userId);

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

        UpdateCounter data = new UpdateCounter(
                userId,
                CounterType.FOLLOWING,
                -1
        );

        counterClient.denormalize(data,secret);

        UpdateCounter data2 = new UpdateCounter(
                followedId,
                CounterType.FOLLOWER,
                -1
        );

        counterClient.denormalize(data2,secret);

        relationRepository.delete(follower);

        interactonService.deleteInteraction(followedId,userId);

    }

    @Override
    public void removeFollower(String followedById, String userId) {
        Follower follower = relationRepository
                .findByUserIdAndFollowedId(followedById, userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No follow found"));

        UpdateCounter data = new UpdateCounter(
                followedById,
                CounterType.FOLLOWING,
                -1
        );

        counterClient.denormalize(data,secret);

        UpdateCounter data2 = new UpdateCounter(
                userId,
                CounterType.FOLLOWER,
                -1
        );

        counterClient.denormalize(data2,secret);

        relationRepository.delete(follower);

        interactonService.deleteInteraction(userId,followedById);
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


