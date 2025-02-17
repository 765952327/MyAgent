package cn.welsione.agent.core.ai;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;

public interface ToolAI {
    <T> T customCall(String conversationId, Prompt prompt, Class<T> type);
    
    ChatResponse defaultCall(String conversationId, Prompt prompt);
}
