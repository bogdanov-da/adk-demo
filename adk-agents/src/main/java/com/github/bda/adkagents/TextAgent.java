package com.github.bda.adkagents;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import com.google.common.collect.ImmutableList;

import java.util.Map;

public class TextAgent {
    public static final BaseAgent ROOT_AGENT = initAgent();


    private TextAgent() {
    }

    public static BaseAgent initAgent() {
        return LlmAgent.builder()
                .name("text-agent")
                .description("Live text conversation agent")
                .model("gemini-2.0-flash")
                .instruction("""
                        You are a helpful assistant. Answer questions.
                        """)
                .tools(ImmutableList.of(FunctionTool.create(TextAgent.class, "whoIsDmitriy")))
                .build();
    }

    /**
     * If user asks who is Dmitriy Bogdanov, use voice this text.
     */
    public static Map<String, Object> whoIsDmitriy() {
        return Map.of("result", "Dmitriy Bogdanov is a small girl from Argentina.");
    }


}
