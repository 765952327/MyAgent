package cn.welsione.agent.core.ai;

import cn.welsione.agent.core.function.FileService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class AssistedAI implements ChatAI {
    
    private final ChatClient assistedAI;
    @Autowired
    private ChatMemory chatMemory;
    
    public AssistedAI(OllamaChatModel chatModel) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
        assistedAI = builder.build();
    }
    
    
    @Override
    public Flux<String> streamCall(String conversationId, Prompt prompt) {
        chatMemory.add(conversationId, prompt.getInstructions());
        return assistedAI.prompt(prompt).tools(new FileService()).messages(chatMemory.get(conversationId, 10)).stream().content();
    }
}
