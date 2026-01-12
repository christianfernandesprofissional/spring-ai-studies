package com.springAi.controller;

import com.springAi.model.CountryCities;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * Classe usada para demonstrar o uso da IA para gerar
 * classes a ajudar na construção das classes de modelo
 * de negócio
 */
@RestController
@RequestMapping("/api")
public class StructuredOutputController {

    private final ChatClient chatClient;

    public StructuredOutputController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping("/chat-bean")
    public ResponseEntity<CountryCities> chatBean(@RequestParam("message") String message) {
        CountryCities countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(CountryCities.class); //Criando a classe através da resposta da LLM
        return ResponseEntity.ok(countryCities);
    }

    @GetMapping("/chat-list")
    public ResponseEntity<List<String>> chatList(@RequestParam("message") String message) {
        List<String> countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new ListOutputConverter()); //Converter a resposta da LLM em uma lista
        return ResponseEntity.ok(countryCities);
    }

    @GetMapping("/chat-map")
    public ResponseEntity<Map<String,Object>> chatMap(@RequestParam("message") String message) {
        Map<String, Object> countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new MapOutputConverter()); //Converter a resposta da LLM em um objeto MAP
        return ResponseEntity.ok(countryCities);
    }

    @GetMapping("/chat-bean-list")
    public ResponseEntity<List<CountryCities>> chatBeanList(@RequestParam("message") String message) {
        List<CountryCities> countryCities = chatClient
                .prompt()
                .user(message)
                .call().entity(new ParameterizedTypeReference<List<CountryCities>>() {
                }); //Converter a resposta da LLM em uma lista de objetos do tipo desejado
        return ResponseEntity.ok(countryCities);
    }

}
