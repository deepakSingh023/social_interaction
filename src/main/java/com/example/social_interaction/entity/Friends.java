package com.example.social_interaction.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Date;

@Document(collection = "friends")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Friends {
    @Id
    private String id;

    private String senderId;

    private String senderAvatar;

    private String senderName;

    private String receiverId ;

    private String receiverName;

    private String receiverAvatar;

    private Instant acceptedAt;

}
