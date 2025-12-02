package com.github.bda.adkagents;

import com.google.adk.agents.BaseAgent;
import com.google.adk.web.AdkWebServer;

public class App {

    public static void main( String[] args)
    {
        BaseAgent audioAgent = StreamingAgent.ROOT_AGENT;
        BaseAgent textAgent = TextAgent.ROOT_AGENT;
        AdkWebServer.start(textAgent, audioAgent);
    }
}
