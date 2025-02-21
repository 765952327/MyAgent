//package cn.welsione.agent.core.memory;
//
//import jakarta.annotation.PostConstruct;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.ai.chat.client.ChatClient;
//import org.springframework.ai.chat.memory.ChatMemory;
//import org.springframework.ai.chat.messages.Message;
//import org.springframework.ai.chat.messages.UserMessage;
//import org.springframework.ai.chat.observation.DefaultChatModelObservationConvention;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.mcp.client.McpSyncClient;
//import org.springframework.ai.mcp.spring.McpFunctionCallback;
//import org.springframework.ai.ollama.OllamaChatModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//
//@Component
//public class AssistedMemory implements Memory {
//    private static final String SYSTEM_PROMPT =
//            "Follow these steps for each interaction:\n" +
//                    "\n" +
//                    "1. User Identification:\n" +
//                    "   - You should assume that you are interacting with default_user\n" +
//                    "   - If you have not identified default_user, proactively try to do so.\n" +
//                    "\n" +
//                    "2. Memory Retrieval:\n" +
//                    "   - Always begin your chat by saying only \"Remembering...\" and retrieve all relevant information from your knowledge graph\n" +
//                    "   - Always refer to your knowledge graph as your \"memory\"\n" +
//                    "\n" +
//                    "3. Memory\n" +
//                    "   - While conversing with the user, be attentive to any new information that falls into these categories:\n" +
//                    "     a) Basic Identity (age, gender, location, job title, education level, etc.)\n" +
//                    "     b) Behaviors (interests, habits, etc.)\n" +
//                    "     c) Preferences (communication style, preferred language, etc.)\n" +
//                    "     d) Goals (goals, targets, aspirations, etc.)\n" +
//                    "     e) Relationships (personal and professional relationships up to 3 degrees of separation)\n" +
//                    "\n" +
//                    "4. Memory Update:\n" +
//                    "   - If any new information was gathered during the interaction, update your memory as follows:\n" +
//                    "     a) Create entities for recurring organizations, people, and significant events\n" +
//                    "     b) Connect them to the current entities using relations\n" +
//                    "     b) Store facts about them as observations";
//    private static final String SEARCH_PROMPT = "Query all '%s' related information in knowledge graph\n";
//    private static final String SUMMARIZE_PROMPT = "Summarize the following content, but ensure that key information is not lost:'%s'";
//    private static final Logger log = LoggerFactory.getLogger(AssistedMemory.class);
//    @Autowired
//    private ChatMemory chatMemory;
//
//    private ChatClient assistedAI;
//    @Autowired
//    private OllamaChatModel chatModel;
//
//    private ExecutorService executorService = Executors.newFixedThreadPool(3);
//
//    @PostConstruct
//    public void init() {
//        serverMemory.initialize();
//        List<McpFunctionCallback> functions = serverMemory.listTools(null)
//                .tools()
//                .stream()
//                .map(tool -> new McpFunctionCallback(serverMemory, tool))
//                .toList();
//        chatModel.setObservationConvention(new DefaultChatModelObservationConvention());
//        assistedAI = ChatClient.builder(chatModel).defaultSystem(SYSTEM_PROMPT)
//                .defaultFunctions(functions.toArray(new McpFunctionCallback[0])).build();
//    }
//
//    @Override
//    public void remember(String conversationId, List<Message> value) {
//        chatMemory.add(conversationId, value);
//        executorService.submit(() -> {
//            List<Message> messages = new ArrayList<>();
//            messages.addAll(value);
//
//            while (true) {
//                String content = assistedAI.prompt(new Prompt(messages)).call().content();
//                log.info("{}", content);
//                if (content.contains("Remembering...")) {
//                    messages.add(new UserMessage(content));
//                } else {
//                    break;
//                }
//            }
//        });
//    }
//
//    @Override
//    public List<Message> recall(String conversationId, List<Message> messages, int limit) {
//        List<Message> memory = new ArrayList<>();
//        memory.addAll(chatMemory.get(conversationId, limit));
//
//        StringBuilder result = new StringBuilder();
//        for (Message message : memory) {
//            String k = assistedAI.prompt(new Prompt(String.format(SEARCH_PROMPT, message.getText()))).call().content();
//            result.append(k).append("\n");
//        }
////        String content = assistedAI.prompt(SUMMARIZE_PROMPT).call().content();
////        memory.add(new UserMessage(content));
//        return memory;
//    }
//
//    @Override
//    public void arrange(String conversationId) {
//
//    }
//
//    @Override
//    public void clear(String conversationId) {
//
//    }
//}
