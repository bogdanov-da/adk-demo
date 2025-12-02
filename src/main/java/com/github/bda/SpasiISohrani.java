package com.github.bda;

import org.springaicommunity.mcp.annotation.McpTool;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class SpasiISohrani {

    @Tool(description = "Answer who is Dmitriy Bogdanov")
    public String whoIsDmitriy() {
        return "Dmitriy Bogdanov is a small girl from Argentina.";
    }
}
