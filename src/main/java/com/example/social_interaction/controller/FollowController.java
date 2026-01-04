package com.example.social_interaction.controller;



import com.example.social_interaction.entity.Follower;
import com.example.social_interaction.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/relations")
@RequiredArgsConstructor
public class FollowController {

    private final RelationService relationService;

    @PostMapping("/follow")
    public ResponseEntity<Void> follow(
            @RequestParam String userId,
            @RequestParam String followedId,
            @RequestParam Boolean prvAcc
    ) {
        relationService.followRequest(userId, followedId, prvAcc);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/follow-requests/{requestId}/accept")
    public ResponseEntity<Void> acceptFollowRequest(
            @PathVariable String requestId
    ) {
        relationService.acceptFollowRequest(requestId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/follow-requests/{requestId}/reject")
    public ResponseEntity<Void> rejectFollowRequest(
            @PathVariable String requestId
    ) {
        relationService.rejectFollowRequest(requestId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/unfollow")
    public ResponseEntity<Void> stopFollowing(
            @RequestParam String userId,
            @RequestParam String followedId
    ) {
        relationService.stopFollowing(userId, followedId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/remove-follower")
    public ResponseEntity<Void> removeFollower(
            @RequestParam String userId,
            @RequestParam String followedById
    ) {
        relationService.removeFollower(followedById, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<Follower>> getFollowers(
            @PathVariable String userId
    ) {
        return ResponseEntity.ok(relationService.getFollowers(userId));
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<Follower>> getFollowing(
            @PathVariable String userId
    ) {
        return ResponseEntity.ok(relationService.getFollowing(userId));
    }
}
