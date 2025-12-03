package com.github.bda.mcpserver;

import com.github.bda.tools.SpasiISohrani;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
@ComponentScan(basePackages = {"com.github.bda.mcpserver", "com.github.bda.tools"})
public class McpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }

    @Bean
    public List<ToolCallback> myMcpTools(SpasiISohrani spasiISohrani) {
        return List.of(ToolCallbacks.from(spasiISohrani));
    }

}
