package cn.welsione.agent.core.ai;

import java.util.List;
import lombok.Builder;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.ChatOptions;

@Builder
public class AIBuilder {
    private ChatModel model;
    private List<SystemMessage> systemMessages;
    private List<AssistantMessage> assistantMessages;
    private List<ToolResponseMessage> toolResponseMessages;
    private List<ChatOptions> options;
}
