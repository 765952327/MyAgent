package cn.welsione.agent.core.cerebellum.instinct;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.springframework.stereotype.Component;

@Component
public class SimpleWriteInstinct implements Instinct {
    
    private static final String name = "simpleWrite";
    private static final String vector = "写文件";
    private static final String description = "将需要的内容写入文件中指定文件中";
    private static final String[] instructions = {"写文件","向指定文件写入内容"};
    
    @Override
    public void execute(String[] args) {
        String path = args[0];
        String context = args[1];
        File file = new File(path);
        try {
            Files.writeString(file.toPath(), context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    public String name() {
        return name;
    }
    
    @Override
    public String description() {
        return description;
    }
    
    @Override
    public String[] instructions() {
        return instructions;
    }
    
    @Override
    public String[] params() {
        return new String[]{"文件路径","写入内容"};
    }
}
