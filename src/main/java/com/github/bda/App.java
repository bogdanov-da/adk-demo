package com.github.bda;

import com.google.adk.agents.BaseAgent;
import com.google.adk.web.AdkWebServer;
import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import samples.liveaudio.StreamingAgent;
import samples.liveaudio.TextAgent;

import java.util.List;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App {

    public static void main( String[] args)
    {
      //  SpringApplication.run(App.class, args);
        BaseAgent audioAgent = StreamingAgent.ROOT_AGENT;
        BaseAgent textAgent = TextAgent.ROOT_AGENT;
        AdkWebServer.start(textAgent, audioAgent);
    }

//    @Bean
//    public CommandLineRunner startAdk() {
//        BaseAgent audioAgent = StreamingAgent.ROOT_AGENT;
//        return args -> {
//            new Thread(() -> {
//                System.out.println("Starting ADK Web Server...");
//                AdkWebServer.start(audioAgent);
//            }).start();
//        };
//    }


    @Bean
    public List<ToolCallback> myMcpTools(SpasiISohrani spasiISohrani) {
        return List.of(ToolCallbacks.from(spasiISohrani));
    }
}
