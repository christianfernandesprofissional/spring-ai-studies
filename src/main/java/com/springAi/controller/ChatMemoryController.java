package com.springAi.controller;

import org.apache.coyote.Response;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api")
public class ChatMemoryController {

    private final ChatClient chatClient;


    //Como precisamos usar outra config nesse controller estamos especificando qual pelo @Qualifier
    public ChatMemoryController(@Qualifier("chatMemoryChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    @GetMapping("/chat-memory")
    public ResponseEntity<String> chatMemory(@RequestHeader("username") String username,
                                             @RequestParam("message") String message){
        return ResponseEntity.ok(chatClient.prompt().user(message)
                        .advisors(
                                //passando o valor de username contido no header como ID, cada chamada é separada internamente,
                                // mas passando um ID (que poderia ser qualquer valor) conseguimos fazer com que a LLM "lembre"
                                // de conversas já feitas dependendo do ID
                                //Com esta implementação lembre-se que a memória será perdida caso o programa reinicie
                                advisorSpec -> advisorSpec.param(CONVERSATION_ID, username)
                        )
                .call().content());
    }

}
