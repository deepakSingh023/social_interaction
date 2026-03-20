package com.example.social_interaction.service;

import com.example.social_interaction.entity.Feed;
import com.example.social_interaction.repository.FeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;


@RequiredArgsConstructor
@Service
public class InteractonService {


    private final FeedRepository feedRepository;



    @Async
    public void createInteraction(String authorId, String recipientId){

        if(feedRepository.existsByAuthorIdOrRecipientUserId(authorId,recipientId)){
            throw new ResponseStatusException(HttpStatus.ALREADY_REPORTED,"interaction already exist for these user");
        }

        Feed feed = Feed.builder()
                .authorId(authorId)
                .recipientUserId(recipientId)
                .createdAt(Instant.now())
                .build();


        feedRepository.save(feed);
    }
}
