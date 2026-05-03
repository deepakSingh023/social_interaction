package com.example.social_interaction.service;


import com.example.social_interaction.dto.InteractionDto;
import com.example.social_interaction.dto.UpdateCounter;
import com.example.social_interaction.tasks.CounterClient;
import com.example.social_interaction.tasks.PostClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class DenormalizeAndFeedService {

    private final CounterClient counterClient;

    private final PostClient postClient;

    @Value("${service.secret}")
    private String secret;


    @Async
    public void worker(UpdateCounter data1, UpdateCounter data2, InteractionDto data3, InteractionDto data4){

        counterClient.denormalize(data1,secret);

        counterClient.denormalize(data2,secret);

        postClient.createFeed(data3,secret);

        postClient.createFeed(data4,secret);


    }


    @Async
    public void followerWorker(UpdateCounter data1, UpdateCounter data2, InteractionDto data3){

        counterClient.denormalize(data1,secret);

        counterClient.denormalize(data2,secret);

        postClient.createFeed(data3,secret);

    }
}
