package com.example.social_interaction.service;

import com.example.social_interaction.entity.FriendRequest;
import com.example.social_interaction.entity.Friends;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendService {

    // Send friend request or auto-accept if inverse request exists
    void addFriend(String senderId, String receiverId);

    // Remove an existing friend
    void removeFriend(String senderId, String receiverId);

    // Get all friends of a user (paginated)
    Page<Friends> getFriends(String userId, Pageable pageable);

    // Accept a friend request (must be receiver)
    void acceptRequest(String requestId, String currentUserId);

    // Reject a friend request (must be receiver)
    void rejectRequest(String requestId, String currentUserId);

    // Get incoming friend requests (paginated)
    Page<FriendRequest> getRequests(String userId, Pageable pageable);
}
