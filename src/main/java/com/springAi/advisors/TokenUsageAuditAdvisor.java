package com.springAi.advisors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatResponse;

/**
 * Classe usada para demonstração de como implementar seu próprio Advisor
 */
public class TokenUsageAuditAdvisor implements CallAdvisor {

    private static final Logger logger = LoggerFactory.getLogger(TokenUsageAuditAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest); //Chamo a LLM passando a mensagem do usuário. Eu não fiz nada com a mensagem do usuário antes de chamar a LLM, mas poderia ter feito
        ChatResponse chatResponse = chatClientResponse.chatResponse(); //Pego a resposta dada pela LLM

        if(chatResponse.getMetadata() != null){ //Nem toda LLM fornece metadados por isso o teste
            Usage usage = chatResponse.getMetadata().getUsage(); // Objeto que representa tokens de entrada, saída e o total de tokens

            if(usage != null){// Nem toda LLM fornece informações de uso por isso o teste
                logger.info("Token usage details : {}",usage.toString());
            }

        }

        return chatClientResponse;
    }

    @Override
    public String getName() {
        return "TokenUsageAuditAdvisor";
    }

    @Override
    public int getOrder() {
        return 1; //Esse número significa a preferência na hora da execução
    }
}
