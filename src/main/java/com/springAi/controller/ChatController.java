package com.springAi.controller;

import com.springAi.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient chatClient){
        this.chatClient = chatClient; //Nova configuração após passar as configurações para ChatClientConfig

       /* Essa configuração abaixo foi passada para a classe ChatClientConfig
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        You are an internal IT helpdesk assistant. Your role is to assist\s
                         employees with IT-related issues such as resetting passwords,\s
                         unlocking accounts, and answering questions related to IT policies.
                         If a user requests help with anything outside of these\s
                         responsibilities, respond politely and inform them that you are\s
                         only able to assist with IT support tasks within your defined scope.
                        """) //Texto padrão inicial do Sistema para todas as chamadas da IA
                //.defaultUser("How can you help me?") mensagem padrão do usuário caso necessário
                .build();
        */
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message){
        return chatClient //Chamada da IA passando Roles com um prompt antes da mensagem do usuário
                .prompt()
                //.options()//Exemplo de uso dos chatOptions eu posso passar ChatOptions dentro desse metodo
                //.advisors(new TokenUsageAuditAdvisor()) //Advisor personalizado sendo passado manualmente fora da classe de configuração (não recomendado)
                /*.system("""
                         You are an internal IT helpdesk assistant. Your role is to assist\s
                         employees with IT-related issues such as resetting passwords,\s
                         unlocking accounts, and answering questions related to IT policies.
                         If a user requests help with anything outside of these\s
                         responsibilities, respond politely and inform them that you are\s
                         only able to assist with IT support tasks within your defined scope.
                        """) // Como eu passei um DefaultSystem no construtor eu não preciso passar esse texto a não ser que eu queira sobrepor a configuração padrão
                */
                .user(message)
                .call().content();

        //return chatClient.prompt(message).call().content(); //Chamada simples da IA
    }

    @GetMapping("/chat2")
    public String chat2(@RequestParam("message") String message){
        return chatClient //Chamada da IA passando Roles com um prompt antes da mensagem do usuário
                .prompt()
                .user(message)
                .call() // Abaixo outras opções além do metodo .content();
                //.chatResponse() //Traz alguns metadados do LLM
                //.chatClientResponse() //Metodo usado em cenarios onde há o uso de RAG, ele retorna um opjeto de ChatClientResponse
                //.entity() //Dessa maneira pedimos a LLM para retonar um JSON Object que será convertido pelo Spring AI em um POJO usado a biblioteca Jackson
                .content();
        //return chatClient.prompt(message).call().content(); //Chamada simples da IA
    }

}
