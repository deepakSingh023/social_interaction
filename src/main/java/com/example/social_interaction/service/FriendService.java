package com.example.social_interaction.service;


import com.example.social_interaction.entity.Friends;

import java.util.List;

public interface FriendService {

    void addFriend(String senderId , String receiverId);
    void removeFriend(String senderId, String receiverId);
    List<Friends> getFriends(String userId);
}
