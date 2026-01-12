package com.springAi.config;

import com.springAi.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder){
        // ChatOptions chatOptions= ChatOptions.builder().model("gpt-4-1-mini").maxTokens(100).temperature(0.8).build(); //Exemplo de uso dos chatOptions


        return chatClientBuilder
                // .defaultOptions(chatOptions) //Exemplo de uso dos chatOptions
                .defaultAdvisors(List.of(new SimpleLoggerAdvisor(), new TokenUsageAuditAdvisor())) // Outros advisors -> SafeGuardAdvisor. Poderia passar este advisor em outro lugar, sem ser no config, mas em um controller por exemplo
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
    }

}
