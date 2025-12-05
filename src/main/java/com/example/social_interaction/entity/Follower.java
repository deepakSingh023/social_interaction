package com.example.social_interaction.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "followers")

public class Follower {

    @Id
    private String id;

    private String userId;

    private String followerId;

    private Date createdAt;

}
