package com.example.social_interaction.service;
import com.example.social_interaction.entity.Follower;
import com.example.social_interaction.repository.RelationRepository;
import com.example.social_interaction.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.time.LocalTime.now;

@Service
@AllArgsConstructor
public class FollowService implements RelationService{


  private final RelationRepository relationRepository;

  private final UserRepository userRepository;


  @Override
  public void followRequest(String userId, String followedId){


      if (!userRepository.existsById(userId)){
          throw new IllegalArgumentException("Sender user does not exist");
      }

      if (!userRepository.existsById(followedId)) {
          throw new IllegalArgumentException("Receiver user does not exist");
      }

      if (followedId.equals(userId)) {
          throw new IllegalArgumentException("You cannot send friend request to yourself");
      }

      if(relationRepository.existsByUserIdAndFollowedId(userId, followedId)){
          throw new ResponseStatusException(
                  HttpStatus.CONFLICT,
                  "Already following");

      }

      Follower f = Follower.builder()
              .userId( userId)
              .followedId(followedId)
              .createdAt(new Date())
              .build();

      relationRepository.save(f);
  }

  @Override
  public void stopFollowing(String followedId,String userId) {// this method is for a person to stop following someone
      Follower follower = relationRepository
              .findByUserIdAndFollowedId(userId,followedId)
              .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No follow found"));

      relationRepository.delete(follower);
  }

    @Override
    public void removeFollower(String userId ,  String followedById) {//this methos is for a person to stop someone or remove someone from his followers
        Follower follower = relationRepository
                .findByUserIdAndFollowedId(followedById , userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No follow found"));

        relationRepository.delete(follower);
    }





  @Override
    public List<Follower> getFollowing(String userId){
      return relationRepository.findByUserId(userId);
  }

  @Override
    public List<Follower> getFollowers(String userId){
      return  relationRepository.findByFollowedId(userId);
  }
}

