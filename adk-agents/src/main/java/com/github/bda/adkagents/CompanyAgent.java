package com.github.bda.adkagents;

import com.google.adk.agents.LlmAgent;
import com.google.adk.agents.ParallelAgent;
import com.google.adk.agents.SequentialAgent;
import com.google.adk.tools.AgentTool;
import com.google.adk.tools.GoogleSearchTool;
import org.springframework.context.annotation.Bean;

public class CompanyAgent {

    @Bean
    public SequentialAgent initAgent() {
        var companyProfiler = LlmAgent.builder()
                .name("company-profiler")
                .description("Provides a comprehensive overview of a company including mission, headquarters, history, and key facts.")
                .instruction("""
                Your role is to provide a detailed overview of the specified company.
                
                Research and include the following information:
                - Company mission and vision statements
                - Headquarters location and key office locations
                - Brief company history and founding information
                - Industry and market segment
                - Company size (employees, revenue if publicly available)
                - Key leadership information
                
                Use the Google Search Tool to find accurate, up-to-date information.
                Focus on official company sources when possible.
                Present the information in a structured, professional format.
                """)
                .model("gemini-2.5-flash")
                .tools(new GoogleSearchTool())
                .outputKey("profile")
                .build();

        var newsFinder = LlmAgent.builder()
                .name("news-finder")
                .description("Finds the latest news and developments about a company.")
                .instruction("""
                Your role is to find the top 4-5 most recent and relevant news headlines about the specified company.
                
                Search for:
                - Recent press releases and announcements
                - Industry news mentioning the company
                - Partnership announcements
                - Product launches or updates
                - Leadership changes or strategic decisions
                
                Use the Google Search Tool to find current information.
                Present results as a bulleted list with:
                - Headline/title
                - Brief summary (1-2 sentences)
                - Date (if available)
                - Source
                
                Focus on news from the last 6-12 months for relevance.
                """)
                .model("gemini-2.5-flash")
                .tools(new GoogleSearchTool())
                .outputKey("news")
                .build();

        var servicesProductsFinder = LlmAgent.builder()
                .name("services-products-finder")
                .description("Identifies and analyzes the company's main products and services.")
                .instruction("""
                Your role is to research and document the company's core products and services.
                
                Research and include:
                - Main product lines and their descriptions
                - Key services offered
                - Target markets for each product/service
                - Unique selling propositions or competitive advantages
                - Technology stack or platforms used (if applicable)
                - Pricing models (if publicly available)
                
                Use the Google Search Tool to find comprehensive information.
                Organize the information by product/service categories.
                Include both current offerings and recently announced products/services.
                """)
                .model("gemini-2.5-flash")
                .tools(new GoogleSearchTool())
                .outputKey("products_services")
                .build();

        var dataCollector = ParallelAgent.builder()
                .name("data-collector")
                .description("Collects all company information in parallel.")
                .subAgents(companyProfiler, newsFinder, servicesProductsFinder)
                .build();

        var reportCompiler = LlmAgent.builder()
                .name("report-compiler")
                .description("Compiles a comprehensive company research report.")
                .instruction("""
                Your role is to synthesize all provided information into a comprehensive, well-structured company research report.
                
                Create a professional report with the following structure:
                
                # Company Research Report
                
                ## Executive Summary
                Provide a 2-3 sentence high-level overview of the company and its current position.
                
                ## Company Profile
                {profile}
                
                ## Products and Services
                {products_services}
                
                ## Recent News and Developments
                {news}
                
                ## Key Insights and Analysis
                Provide 3-4 key insights about the company based on all the gathered information, including:
                - Market position and competitive advantages
                - Growth opportunities and challenges
                - Notable trends or patterns
                
                ## Conclusion
                Summarize the company's current status and outlook.
                
                Ensure the report is professional, well-formatted, and provides actionable insights.
                Use clear headings and organize information logically.
                """)
                .model("gemini-2.5-flash")
                .build();

        return SequentialAgent.builder()
                .name("company-research-agent")
                .description("Conducts comprehensive research and analysis of any company, generating detailed reports covering company profile, products/services, recent news.")
                .subAgents(
                        dataCollector,
                        reportCompiler
                )
                .build();
    }
}
