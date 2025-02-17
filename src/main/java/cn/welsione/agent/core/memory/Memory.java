package cn.welsione.agent.core.memory;

import java.util.List;
import org.springframework.ai.chat.messages.Message;

/**
 * 记忆接口
 */
public interface Memory {
    void remember(String conversationId, List<Message> value);
    
    List<Message> recall(String conversationId, List<Message> messages, int limit);
    
    void arrange(String conversationId);
    
    void clear(String conversationId);
}
