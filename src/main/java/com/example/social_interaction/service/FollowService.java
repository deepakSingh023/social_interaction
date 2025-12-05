package com.example.social_interaction.service;


import com.example.social_interaction.dto.FollowerRequestDTO;
import com.example.social_interaction.dto.RemoveFollower;
import com.example.social_interaction.entity.Follower;
import com.example.social_interaction.repository.RelationRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;

import static java.time.LocalTime.now;

@Service
@AllArgsConstructor
public class FollowService implements RelationService{


  private final RelationRepository relationRepository;


  @Override
  public void followRequest(FollowerRequestDTO data){

      if(relationRepository.existsByUserIdAndFollowerId(data.getUserId(), data.getFollowerId())){
          throw new ResponseStatusException(
                  HttpStatus.CONFLICT,
                  "Already following");

      }

      Follower f = Follower.builder()
              .userId(data.getUserId())
              .followerId(data.getFollowerId())
              .createdAt(new Date())
              .build();

      relationRepository.save(f);
  }

  @Override
  public void removeFollower(RemoveFollower data) {
      Follower follower = relationRepository
              .findByUserIdAndFollowerId(data.getUserId(), data.getFollowerId())
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No follow found"));

      relationRepository.delete(follower);
  }
}
