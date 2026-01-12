package com.springAi.config;

import com.springAi.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Classe para demonstração de como funciona a memória em LLM's
 */
@Configuration
public class ChatMemoryChatClientConfig {



    @Bean //Criando seu próprio Bean de ChatMemory para que seja possível configurar as opções que voce quer
    ChatMemory chatMemory(JdbcChatMemoryRepository jdbcChatMemoryRepository){
        return MessageWindowChatMemory.builder().maxMessages(10) //Configurar o maximo de mensagem com um número muito grande não é recomendado a não ser que seja realmente necessário, pois pode custar caro e também devido ao limite da própria LLM
                .chatMemoryRepository(jdbcChatMemoryRepository).build();
    }

    // Dando nome para o Bean para que não ocorra erro
    //na incialização devido a outra classe de config existir
    @Bean("chatMemoryChatClient")
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory){

        //Criando os Advisors para que eles entrem na lista de advisors padrão
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();


        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor)) //Passando os advisors
                .build();

    }

}
