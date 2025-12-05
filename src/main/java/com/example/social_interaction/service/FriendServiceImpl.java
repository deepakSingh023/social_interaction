package com.example.social_interaction.service;


import com.example.social_interaction.dto.FriendRequestDTO;
import com.example.social_interaction.entity.FriendRequest;
import com.example.social_interaction.repository.FriendRepository;
import com.example.social_interaction.repository.FriendRequestRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FriendServiceImpl implements FriendService{

    private final FriendRequestRepository friendRequestRepository;

    @Override
    public void addFriend(FriendRequestDTO data){

        FriendRequest request = friendRequestRepository.
                findBySenderIdAndReceiverIdOrReceiverIdAndSenderId(data.getUserId(), data.getReceiverId(), data.getReceiverId(), data.getUserId()).
                if(!request){

                }


    }


}
