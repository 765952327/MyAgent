package cn.welsione.agent.core.ai;

import cn.welsione.agent.core.memory.AssistedMemory;
import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.function.FunctionCallback;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class AssistedAI implements ChatAI {
//    @Autowired
//    private List<FunctionCallback> functions;
    @Autowired
    private AssistedMemory memory;
    private final ChatClient assistedAI;
    
    public AssistedAI(OllamaChatModel chatModel) {
        ChatClient.Builder builder = ChatClient.builder(chatModel);
//        if (functions != null && !functions.isEmpty()){
//            builder.defaultFunctions(functions.toArray(new FunctionCallback[0]));
//        }
        assistedAI = builder.build();
    }
    
    
    @Override
    public Flux<String> streamCall(String conversationId, Prompt prompt) {
        memory.remember(conversationId, prompt.getInstructions());
        List<Message> recall = memory.recall(conversationId, prompt.getInstructions(), 10);
        return assistedAI.prompt(prompt).messages(recall).stream().content();
    }
    
    @Override
    public <T> T customCall(String conversationId, Prompt prompt, Class<T> type) {
        return null;
    }
    
    @Override
    public ChatResponse defaultCall(String conversationId, Prompt prompt) {
        return null;
    }
}
