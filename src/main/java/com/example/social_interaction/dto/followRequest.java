package com.example.social_interaction.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class followRequest {

    private String userId;

    private String userAvatar;

    private String userName;

    private String followedId;

    private String followedAvatar;

    private String followedName;

    private Boolean prvAcc;
}
