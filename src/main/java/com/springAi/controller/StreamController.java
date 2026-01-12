package com.springAi.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api")
public class StreamController {

    private final ChatClient chatClient;

    public StreamController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/stream")
    public Flux<String> stream(@RequestParam("message") String message) {
        return chatClient.prompt().user(message)
                .stream()// Com este metodo, a resposta gerada pela IA é mostrada conforme é construida, como se ela tivesse escrevendo, isso também é possível graças ao Flux
                .content();

        /**
         * Sobre a classe Flux (Reactive Programming)
         *
         * Flux é o “List assíncrono” do mundo reativo.
         *
         * O Flux serve para lidar com:
         * Streams de dados
         * Operações assíncronas
         * Grandes volumes de dados
         * Dados que chegam com o tempo
         * Sistemas não-bloqueantes
         *
         * Neste metodo o Flux recebe a resposta da LLM de forma assíncrona
         * e exibe conforme a resposta é gerada
         */
    }
}