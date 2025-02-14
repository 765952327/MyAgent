package cn.welsione.agent.core.cerebellum.instinct;

import cn.hutool.core.io.FileUtil;
import java.io.IOException;
import org.springframework.stereotype.Component;

@Component
public class SimpleCreateInstinct implements Instinct {
    @Override
    public String name() {
        return "createInstinct";
    }
    
    @Override
    public String description() {
        return "在指定的路径创建一个新的文件或目录";
    }
    
    @Override
    public String[] instructions() {
        return new String[]{"创建文件", "创建目录"};
    }
    
    @Override
    public String[] params() {
        return new String[]{"文件或目录路径"};
    }
    
    @Override
    public void execute(String[] args) {
        System.out.println("path：" + args[0]);
        try {
            FileUtil.newFile(args[0]).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
