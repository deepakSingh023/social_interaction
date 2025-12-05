package com.example.social_interaction.service;

import com.example.social_interaction.dto.FollowerRequestDTO;
import com.example.social_interaction.dto.FriendRequestDTO;
import com.example.social_interaction.dto.RemoveFollower;

public interface RelationService {

   void  followRequest(FollowerRequestDTO data );
   void  removeFollower(RemoveFollower data);


}
