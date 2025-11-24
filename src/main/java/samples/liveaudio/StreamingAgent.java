package samples.liveaudio;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;

/**
 * Streaming agent definition for the ADK Dev UI / Web server.
 *
 * The Dev UI expects a public static field named ROOT_AGENT that is initialized at declaration time.
 * This class follows the structure shown in the ADK Streaming Quickstart for Java.
 */
public final class StreamingAgent {

  /** The root agent instance used by the ADK Dev UI. */
  public static final BaseAgent ROOT_AGENT = initAgent();

  private StreamingAgent() {}

  public static BaseAgent initAgent() {
    return LlmAgent.builder()
        .name("live-audio-app")
        .description("Live audio conversation agent")
        .model("gemini-2.0-flash-exp")
            .tools()
        .instruction("""
            You are a helpful assistant for a live voice conversation. Keep responses concise.
            """)
        .build();
  }
}
