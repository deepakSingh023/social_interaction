package com.example.social_interaction.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FriendRequestDTO {

    private String userId;

    private String receiverId;

}
