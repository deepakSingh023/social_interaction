package com.example.social_interaction.service;

import com.example.social_interaction.entity.FollowRequest;
import com.example.social_interaction.entity.Follower;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RelationService {

   void  followRequest(String userId , String followedId , Boolean prvAcc );
   void  stopFollowing(String userId , String followedId);
   void removeFollower( String followedById, String userId);
   void acceptFollowRequest(String requestId);
    Page<Follower> getFollowing(String userId, Pageable pageable);
    Page<Follower> getFollowers(String userId, Pageable pageable);
    Page<FollowRequest> getFollowRequests(String userId , Pageable pageable);
    //the people who follow user
     void rejectFollowRequest(String requestId);

}
