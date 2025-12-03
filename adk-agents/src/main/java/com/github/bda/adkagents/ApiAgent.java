package com.github.bda.adkagents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.FunctionTool;
import com.google.common.collect.ImmutableList;
import org.springframework.context.annotation.Bean;
import com.github.bda.services.WialonApiService;

import java.util.Map;

public class ApiAgent {

    private static final WialonApiService wialonApiService = new WialonApiService();

    @Bean
    public LlmAgent initAgent() {
        return LlmAgent.builder()
                .name("wialon-api-agent")
                .description("Agent that can authenticate with Wialon API and create users")
                .instruction("""
                You are a Wialon API agent that can authenticate and create users in the Wialon system.
                
                You can help users with:
                - Logging into Wialon system to get session ID
                - Creating new users in Wialon system
                - Managing the authentication flow
                
                Available operations:
                1. Use 'loginToWialon' to authenticate and get session ID
                2. Use 'createWialonUser' to create new users (requires login first)
                
                Always login first before creating users. If you get authentication errors, 
                try logging in again to refresh the session.
                """)
                //.model("gemini-2.5-flash")
                .tools(ImmutableList.of(
                        FunctionTool.create(ApiAgent.class, "loginToWialon"),
                        FunctionTool.create(ApiAgent.class, "createWialonUser")
                ))
                .build();
    }

    @Schema(description = "Authenticate with Wialon API to get session ID")
    public static Map<String, Object> loginToWialon() {
        return wialonApiService.loginToWialon();
    }

    @Schema(description = "Create a new user in Wialon system (requires authentication first)")
    public static Map<String, Object> createWialonUser(
            @Schema(description = "Name/username for the new user")
            String name,
            @Schema(description = "Password for the new user")
            String password
    ) {
        return wialonApiService.createWialonUser(name, password);
    }
}