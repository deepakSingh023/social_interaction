package com.example.social_interaction.repository;

import com.example.social_interaction.entity.Follower;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface  RelationRepository extends MongoRepository<Follower,String> {

    boolean existsByUserIdAndFollowedId(String userId ,String FollowedId);

    Optional<Follower> findByUserIdAndFollowedId(String userId , String followedId);
    //the user is the person being followed and the follower is the current user

    List<Follower> findByFollowedId(String followedId);

    List<Follower> findByUserId(String userId);


}
