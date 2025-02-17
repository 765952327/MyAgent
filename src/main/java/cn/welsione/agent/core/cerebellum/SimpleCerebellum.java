package cn.welsione.agent.core.cerebellum;

import cn.hutool.json.JSONUtil;
import cn.welsione.agent.core.Option;
import cn.welsione.agent.core.algorithm.KNNUtil;
import cn.welsione.agent.core.cerebellum.instinct.Instinct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;


@Component
public class SimpleCerebellum implements Cerebellum {
    private static final Logger log = LoggerFactory.getLogger(SimpleCerebellum.class);
    private final ChatClient cerebellumAIClient;
    private final Map<String, Instinct> instincts = new HashMap<>();
    
    @Autowired
    private ChatMemory memory;
    
    public SimpleCerebellum(List<Instinct> instincts, @Qualifier(value = "ollamaChatModel") OllamaChatModel model) {
        StringBuilder sb = new StringBuilder()
                .append("你会选择合适的工具来处理，用户提出的任务")
                .append("注意：")
                .append("1. 如果你需要使用工具,请严格按照此格式返回字符串:")
                .append("{\"name\":\"%name\",\"args\":[\"%arg1\",\"%arg2\",...]}")
                .append("其中%name是你需要调用的工具名称，%arg1, %arg2,...是该工具需要的参数,你应当替换这些内容为实际的参数")
                .append("例如:").append("{\"name:\"simpleWrite\",\"args\":[\"/test.txt\",\"Hello, world!\"]}")
                .append("2.如果你不需要使用任何工具，请返回字符串:").append("none")
                .append("下面是你的工具列表:");
        for (Instinct instinct : instincts) {
            sb.append(instinct.name()).append(":").append(instinct.description())
                    .append("功能: ").append(Arrays.toString(instinct.instructions()))
                    .append("参数: ").append(Arrays.toString(instinct.params()));
            this.instincts.put(instinct.name(), instinct);
        }
        cerebellumAIClient = ChatClient.builder(model).defaultSystem(sb.toString()).build();
    }
    
    
//    @Override
//    public String call(String prompt) {
//        Prompt promptObj = new Prompt(prompt);
//        String res = cerebellumAIClient.prompt(promptObj).call().content();
//        if (res.contains("</think>")){
//           res = res.substring(res.indexOf("</think>")).replace("</think>","").trim();
//        }
//        return res;
//    }
//
//    @Override
//    public String call(String prompt, List<String> history) {
//
//    }
    
    
    @Override
    public void execute(String message) {
//        log.info(message);
//        KNNUtil util = new KNNUtil();
//        List<String> knn = util.get(message);
//        StringBuilder sb = new StringBuilder();
//        sb.append("你需要处理的任务：").append(message);
//        if (knn != null && !knn.isEmpty()){
//            sb.append("你的历史记忆：");
//            for (String s : knn) {
//                sb.append(s).append("\n");
//            }
//        }
////        String call = call(sb.toString());
//        if (call.equals("none")){
//            return;
//        }
//        log.info("call:{}",call);
//        try {
//            Option option = JSONUtil.toBean(call, Option.class);
//            instincts.get(option.getName()).execute(option.getArgs());
//            util.put(String.format("执行任务成功,任务:%s,结果:%s", message, call));
//        } catch (Exception e){
//            log.error(e.getMessage());
//            util.put(String.format("执行任务失败,任务:%s,结果:%s,原因:%s",message,call,e.getMessage()));
//        }
        
    }
    
    public Flux<String> call(String conversationId, String prompt) {
        memory.add(conversationId, new UserMessage(prompt));
        Prompt promptObj = new Prompt(prompt);
        List<Message> messages = memory.get(conversationId, 10);
        return cerebellumAIClient.prompt(promptObj).messages(messages).stream().content();
    }
}
