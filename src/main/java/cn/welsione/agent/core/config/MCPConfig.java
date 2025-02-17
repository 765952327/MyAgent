package cn.welsione.agent.core.config;

import org.springframework.ai.mcp.client.McpClient;
import org.springframework.ai.mcp.client.McpSyncClient;
import org.springframework.ai.mcp.client.stdio.ServerParameters;
import org.springframework.ai.mcp.client.stdio.StdioServerTransport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MCPConfig {
    
    
    @Bean("serverMemory")
    public McpSyncClient serverMemory() {
        var stdioParams = ServerParameters.builder("npx")
                .args("-y", "@modelcontextprotocol/server-memory")
                .build();
        
        return McpClient.sync(new StdioServerTransport(stdioParams));
    }
}
