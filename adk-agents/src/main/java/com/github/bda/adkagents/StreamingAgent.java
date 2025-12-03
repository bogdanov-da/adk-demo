package com.github.bda.adkagents;

import com.github.bda.tools.SpasiISohrani;
import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.FunctionTool;
import com.google.common.collect.ImmutableList;
import org.springframework.context.annotation.Bean;

/**
 * Streaming agent definition for the ADK Dev UI / Web server.
 *
 * The Dev UI expects a public static field named ROOT_AGENT that is initialized at declaration time.
 * This class follows the structure shown in the ADK Streaming Quickstart for Java.
 */
public final class StreamingAgent {
    public static final BaseAgent ROOT_AGENT = initAgent();

    private StreamingAgent() {}

    @Bean
    public static LlmAgent initAgent() {
        return LlmAgent.builder()
                .name("live-audio-app")
                .description("Live audio conversation agent")
                //.model("gemini-2.0-flash-live-001")
                .model("gemini-2.5-flash-native-audio-preview-09-2025")
                .instruction("""
                        You are a helpful assistant. Use the available tools to answer questions.
                        """)
                .tools(ImmutableList.of(FunctionTool.create(SpasiISohrani.class, "whoIsDmitriy")))
                .build();
    }
}
