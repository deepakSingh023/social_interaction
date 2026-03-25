package com.example.social_interaction.tasks;


import com.example.social_interaction.dto.InteractionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="ineraction", url="${feed.uri}")
public interface PostClient {

    @PostMapping("/api/feeds/create-feed-iteraction")
     void createFeed(
             @RequestBody InteractionDto data
     );
}
