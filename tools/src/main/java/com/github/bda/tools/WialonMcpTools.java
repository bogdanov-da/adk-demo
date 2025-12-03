package com.github.bda.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;
import com.github.bda.services.WialonApiService;

import java.util.Map;

@Component
public class WialonMcpTools {
    private final WialonApiService wialonApiService;

    public WialonMcpTools(WialonApiService wialonApiService) {
        this.wialonApiService = wialonApiService;
    }

    @Tool(description = "Authenticate with Wialon API to get session ID")
    public Map<String, Object> loginToWialon() {
        return wialonApiService.loginToWialon();
    }

    @Tool(description = "Create a new user in Wialon system (requires authentication first)")
    public Map<String, Object> createWialonUser(String name, String password) {
        return wialonApiService.createWialonUser(name, password);
    }
}
