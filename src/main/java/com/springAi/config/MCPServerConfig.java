package com.springAi.config;

import com.springAi.tools.HelpDeskTools;
import com.springAi.tools.HelpDeskToolsMCPServer;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MCPServerConfig {

    @Bean
    List<ToolCallback> toolCallbacks(HelpDeskToolsMCPServer helpDeskTools, HelpDeskToolsMCPServer helpDeskToolsMCPServer) { //Essa lista de Toolcallback é necessária para expor todas as ferramentas que quisermos no MCP Server
        return List.of(ToolCallbacks.from(helpDeskToolsMCPServer));
    }

}
