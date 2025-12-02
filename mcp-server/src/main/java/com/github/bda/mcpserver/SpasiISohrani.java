package com.github.bda.mcpserver;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class SpasiISohrani {

    @Tool(description = "Answer who is Dmitriy Bogdanov")
    public String whoIsDmitriy() {
        return "Dmitriy Bogdanov is a small girl from Argentina.";
    }
}
