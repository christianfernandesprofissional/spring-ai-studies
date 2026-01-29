package com.springAi.controller.toolCallingAndMCP;


import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mcpClient")
public class MCPClientController {

    private  final ChatClient chatClient;

    public MCPClientController(ChatClient.Builder chatClientBuilder,
                               ToolCallbackProvider toolCallbackProvider) {
        this.chatClient = chatClientBuilder.defaultToolCallbacks(toolCallbackProvider)
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestHeader(value = "username",required = false) String username,
                       @RequestParam("message") String message) {
        return chatClient.prompt()
                .system("""
                        You are an internal IT helpdesk assistant. Your role is to assist\s
                         employees with IT-related issues such as resetting passwords,\s
                         unlocking accounts, and answering questions related to IT policies.
                         If a user requests help with anything outside of these\s
                         responsibilities, respond politely and inform them that you are\s
                         only able to assist with IT support tasks within your defined scope.
                        """)
                .user(message+ " My username is " + username)
                .call().content(); // return adicionado depois de implementar o MCP Server

        //return chatClient.prompt().user(message)
        //        .call().content();
    }

}
