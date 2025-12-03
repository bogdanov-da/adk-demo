package com.github.bda.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SpasiISohrani {

    @Tool(description = "Answer who is Dmitriy Bogdanov")
    public static Map<String, Object> whoIsDmitriy() {
        return Map.of("result", "Dmitriy Bogdanov is a small girl from Argentina.");
    }
}
