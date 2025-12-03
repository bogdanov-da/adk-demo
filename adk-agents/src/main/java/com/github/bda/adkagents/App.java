package com.github.bda.adkagents;

import com.google.adk.agents.BaseAgent;
import com.google.adk.web.AdkWebServer;

public class App {

    public static void main( String[] args)
    {
        BaseAgent audioAgent = StreamingAgent.ROOT_AGENT;
        BaseAgent companyAgent = new CompanyAgent().initAgent();
        BaseAgent apiAgent = new ApiAgent().initAgent();
        AdkWebServer.start(apiAgent, companyAgent, audioAgent);
    }
}
