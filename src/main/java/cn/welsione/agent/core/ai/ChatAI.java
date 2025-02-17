package cn.welsione.agent.core.ai;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

public interface ChatAI extends ToolAI{
    Flux<String> streamCall(String conversationId, Prompt prompt);
    
  
}
