package com.springAi.controller;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/prompt-stuffing")
public class PromptStuffingController {

    private final ChatClient chatClient;

    public PromptStuffingController(ChatClient chatClient){
        this.chatClient = chatClient;
    }


    @Value("classpath:/promptTemplates/systemPromptTemplate.st")
    Resource systemPromptTemplate; //Exemplo de texto do prompt stuffing

    @GetMapping("/email")
    public String emailResponse(@RequestParam("message") String message){

        return chatClient.prompt()
                .system(systemPromptTemplate)
                .user(message)
                .call()
                .content();

    }

}
