package com.springAi.config;

import com.springAi.advisors.TokenUsageAuditAdvisor;
import com.springAi.rag.PIIMaskingDocumentPostProcessor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
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
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder, ChatMemory chatMemory,
                                 RetrievalAugmentationAdvisor retrievalAugmentationAdvisor){

        //Criando os Advisors para que eles entrem na lista de advisors padrão
        Advisor loggerAdvisor = new SimpleLoggerAdvisor();
        Advisor tokenUsageAdvisor = new TokenUsageAuditAdvisor(); //advisor adicionado quando implementei o uso do RAG usando um documento como base de informações
        Advisor memoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();


        return chatClientBuilder
                .defaultAdvisors(List.of(loggerAdvisor, memoryAdvisor, tokenUsageAdvisor, retrievalAugmentationAdvisor)) //Passando os advisors
                .build();

    }



    //Metodo responsavel por configurar um advisor que substitui a lógica de definir
    //como a busca acontecerá em um VectorStore, chamar a busca e transformar o
    //resultado da busca em texto
    @Bean
    RetrievalAugmentationAdvisor retrievalAugmentationAdvisor(VectorStore vectorStore,
                                                              ChatClient.Builder chatClientBuilder) {

        return RetrievalAugmentationAdvisor.builder()
                .queryTransformers( //--Aqui dependendo do transformer passado, irá alterar o prompt antes de ser mandada para a LLM
                        TranslationQueryTransformer.builder() //Transformer para traduzir o prompt
                                .chatClientBuilder(chatClientBuilder.clone())
                                .targetLanguage("english").build()
                ) //--Exemplo de pre-retriever.Existem vários transformers e cada um implementa a interface QueryTransformer

                .documentRetriever( // Utilizando o documentRetriever, estamos informando ao Advisor de onde os documentos precisam ser obtidos,  que podem ser usados como contexto para os modelos de LLM
                        VectorStoreDocumentRetriever.builder().vectorStore(vectorStore)
                        .topK(3).similarityThreshold(0.5).build()
                )
                .documentPostProcessors(PIIMaskingDocumentPostProcessor.builder()) //--Exemplo de Post-retriever
                .build();

    }


}
