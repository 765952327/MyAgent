package cn.welsione.agent.core.ai;

import org.springframework.ai.chat.prompt.Prompt;

public interface SilentAI {
    void silentCall(String conversationId, Prompt prompt);
}
