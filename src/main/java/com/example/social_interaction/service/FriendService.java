package com.example.social_interaction.service;


public interface FriendService {

    void addFriend(String senderId , String receiverId);
    void removeFriend(String senderId, String receiverId);
}
