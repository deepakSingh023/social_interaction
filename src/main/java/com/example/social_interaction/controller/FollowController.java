package com.example.social_interaction.controller;


import com.example.social_interaction.dto.FollowerRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/relation")
public class FollowController {

    @PostMapping("/follow")
    public ResponseEntity<?> follow(
            @RequestBody FollowerRequestDTO data
            ){

    }
}
