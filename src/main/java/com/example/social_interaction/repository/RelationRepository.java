package com.example.social_interaction.repository;

import com.example.social_interaction.entity.Follower;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface  RelationRepository extends MongoRepository<Follower,String> {

    boolean existsByUserIdAndFollowerId(String userId ,String FollowerId);

    Optional<Follower> findByUserIdAndFollowerId(String userId , String followerId);
    //the user is the person being followed and the follower is the current user


}
