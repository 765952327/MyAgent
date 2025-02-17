package cn.welsione.agent.core.client;

import reactor.core.publisher.Flux;

public interface AIClient {
    Flux<String> call(String conversationId, String prompt);
}
