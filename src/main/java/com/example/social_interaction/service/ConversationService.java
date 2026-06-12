package com.example.social_interaction.service;


import com.example.social_interaction.dto.ConversationDto;
import com.example.social_interaction.tasks.ChatClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class ConversationService {


    private final ChatClient chatClient;

    @Value("${service.secret}")
    private String token;


    @Async("conversationUpdate")
    public void createConversation(String senderId, String receiverId){

        ConversationDto conversationDto = new ConversationDto(senderId,receiverId);

        chatClient.createConversation(conversationDto,token);

    }

    @Async("conversationUpdate")
    public void deleteConversation(String senderId, String receiverId){

        ConversationDto conversationDto = new ConversationDto(senderId,receiverId);

        chatClient.deleteConversation(conversationDto,token);

    }
}
