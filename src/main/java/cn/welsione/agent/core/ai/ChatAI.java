package cn.welsione.agent.core.ai;

import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

public interface ChatAI {
    Flux<String> streamCall(String conversationId, Prompt prompt);
}
