package cn.welsione.agent.core.cerebellum;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.json.JSONUtil;
import cn.welsione.agent.core.Option;
import cn.welsione.agent.core.algorithm.KNNUtil;
import cn.welsione.agent.core.cerebellum.instinct.Instinct;
import cn.welsione.agent.core.client.LLMClient;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;


@Component
public class SimpleCerebellum implements Cerebellum, LLMClient {
    private static final Logger log = LoggerFactory.getLogger(SimpleCerebellum.class);
    private final ChatClient openAiClient;
    private final Map<String, Instinct> instincts = new HashMap<>();
    
    public SimpleCerebellum(List<Instinct> instincts, ChatClient.Builder clientBuilder) {
        StringBuilder sb = new StringBuilder()
                .append("你是一个专门用来调用各种工具的助手，你需要严格按照我的指示来调用工具。")
                .append("如果你需要使用哪个工具,请返回以下格式内容:")
                .append("{name:\"%name\",args:[\"%arg1\",\"%arg2\",...]}")
                .append("其中%name是你需要调用的工具名称，%arg1, %arg2,...是该工具需要的参数。")
                .append("例如:").append("{name:\"simpleWrite\",args:[\"./test.txt\",\"Hello, world!\"]}")
                .append("如果你不需要使用任何工具，请返回以下格式内容:").append("{name:\"none\",args:[]}")
                .append("下面是你的工具列表:");
        for (Instinct instinct : instincts) {
            sb.append(instinct.name()).append(":").append(instinct.description())
                    .append("功能: ").append(Arrays.toString(instinct.instructions()))
                    .append("参数: ").append(Arrays.toString(instinct.params()));
            this.instincts.put(instinct.name(), instinct);
        }
        clientBuilder.defaultSystem(sb.toString());
        openAiClient = clientBuilder.build();
    }
    
   
    
    @Override
    public String call(String prompt) {
        log.info("Prompt: {}", prompt);
        return openAiClient.prompt(prompt).call().content();
    }
    
    
    @Override
    public void execute(String message) {
        KNNUtil util = new KNNUtil();
        List<String> knn = util.processString(message);
        StringBuilder sb = new StringBuilder();
        sb.append("你需要处理的任务：").append(message);
        if (knn != null){
            sb.append("你的历史记忆：");
            for (String s : knn) {
                sb.append(s).append("\n");
            }
        }
        String call = call(sb.toString());
        Option option = JSONUtil.toBean(call, Option.class);
        instincts.get(option.getName()).execute(option.getArgs());
    }
}
