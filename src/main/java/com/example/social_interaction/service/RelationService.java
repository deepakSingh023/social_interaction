package com.example.social_interaction.service;

import com.example.social_interaction.entity.Follower;

import java.util.List;

public interface RelationService {

   void  followRequest(String userId , String followedId );
   void  stopFollowing(String userId , String followedId);
   void removeFollower( String followedById, String userId);
   List<Follower> getFollowers(String userId);//the people user follow

   List<Follower> getFollowing(String userId);//the people who follow user


}
