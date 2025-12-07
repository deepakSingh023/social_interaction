package com.example.social_interaction.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relation")
public class FollowController {

    @PostMapping("/follow/{id}")
    public ResponseEntity<?> follow(
           @PathVariable String followedId
            ){

        followrequest();

    }
}
