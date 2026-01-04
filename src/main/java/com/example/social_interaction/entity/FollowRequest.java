package com.example.social_interaction.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Document("follow_requests")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowRequest {

    @Id
    private String id;

    private String userId;

    private String followedId;

    private Instant createdAt;
}
