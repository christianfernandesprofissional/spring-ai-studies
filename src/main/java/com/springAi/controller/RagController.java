package com.springAi.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final ChatClient chatClient;
    //private final ChatClient webSearchChatClient; //Comentado devido a falta do WebSearchRAGChatClientConfig
    private final VectorStore vectorStore;

    @Value("classpath:/promptTemplates/systemPromptRandomDataTemplate.st")
    Resource promptTemplate;

    @Value("classpath:/promptTemplates/systemPromptTemplateRAG.st")
    Resource hrSystemTemplate;

    public RagController(@Qualifier("chatMemoryChatClient") ChatClient chatClient, VectorStore vectorStore){
                         //@Qualifier("webSearchRAGChatClient") ChatClient webSearchChatClient) { //Comentado devido a falta do WebSearchRAGChatClientConfig
        this.chatClient = chatClient;
        this.vectorStore = vectorStore;
        //this.webSearchChatClient = webSearchChatClient; //Comentado devido a falta do WebSearchRAGChatClientConfig
    }

    @GetMapping("/random/chat")
    public ResponseEntity<String> randomChat(@RequestHeader("username") String username,
                                             @RequestParam("message") String message) {

        /* SearchRequest é um objeto de descrição de busca (Search Descriptor).
         * Ela não executa nada.
         * Ela descreve como a busca deve ser executada por um VectorStore.*/
        /*
        SearchRequest searchRequest =
                //-- topK(3) -> número de documentos que serão considerados durante a busca a partir do topo da lista
                //-- .similarityThreshold(0.5) -> porcentagem mínima de similaridade com a mensagem que o documento tem que ter para ser usado durante a busca
              SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5).build();

        List<Document> similarDocs =  vectorStore.similaritySearch(searchRequest); //Busca feita no VectorStore

        //--A linha abaixo pega os documentos retornados pelo Vector Store e transforma em um único texto contínuo, que será usado como contexto para o LLM.
        String similarContext = similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));
        */
        //Toda a lógica mostrada acima pode ser substituida se nas configurações, você implementar um advisor chamado RetrievalAugmentationAdvisor, este advisor foi configurado na classe: ChatMemoryChatClientConfig

        /* --
        String answer = chatClient.prompt()
                .system(promptSystemSpec -> promptSystemSpec.text(promptTemplate) //--configuração de promptSystemSpec desnecessária caso você tenha implementado o RetrievalAugmentationAdvisor
                        .param("documents", similarContext)) //passo a minha template com a lista de frases gerada em RandomDataLoader
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();
         */

        String answer = chatClient.prompt()
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();

        return ResponseEntity.ok(answer);
    }


    //Este controller é igual o randomChat mas usando o contexto vindo do documento Eazybytes_HR_Policies.pdf
    @GetMapping("/document/chat")
    public ResponseEntity<String> documentChat(@RequestHeader("username") String username,
                                               @RequestParam("message") String message) {


        /* //--Trecho comentado após a implementação do RetrievalAugmentationAdvisor que foi configurado na classe ChatMemoryChatClientConfig
        SearchRequest searchRequest =
                SearchRequest.builder().query(message).topK(3).similarityThreshold(0.5).build();
        List<Document> similarDocs =  vectorStore.similaritySearch(searchRequest);
        String similarContext = similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining(System.lineSeparator()));
        String answer = chatClient.prompt()
                .system(promptSystemSpec -> promptSystemSpec.text(hrSystemTemplate) //--configuração de promptSystemSpec desnecessária caso você tenha implementado o RetrievalAugmentationAdvisor
                                .param("documents", similarContext))
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();

         */

        String answer = chatClient.prompt()
                .advisors(a -> a.param(CONVERSATION_ID, username))
                .user(message)
                .call().content();
        return ResponseEntity.ok(answer);
    }


    @GetMapping("/web-search/chat")
    public ResponseEntity<String> webSearchChat(@RequestHeader("username")
                                                String username, @RequestParam("message") String message) {

       // String answer =webSearchChatClient.prompt() //Comentado devido a falta do WebSearchRAGChatClientConfig
       //         .advisors(a -> a.param(CONVERSATION_ID, username)) //Comentado devido a falta do WebSearchRAGChatClientConfig
       //         .user(message) //Comentado devido a falta do WebSearchRAGChatClientConfig
       //         .call().content(); //Comentado devido a falta do WebSearchRAGChatClientConfig
       // return ResponseEntity.ok(answer); //Comentado devido a falta do WebSearchRAGChatClientConfig
        return ResponseEntity.ok("answer");
    }

}
