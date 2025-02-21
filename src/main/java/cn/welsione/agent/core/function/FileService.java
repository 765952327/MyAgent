package cn.welsione.agent.core.function;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class FileService {
    @Tool(name = "创建文件", description = "通过路径创建新文件", returnDirect = true)
    public Boolean createFile(@ToolParam(description = "路径") String path) {
        File file = new File(path);
        if (file.exists()) {
            return false;
        }
        try {
            FileUtil.newFile(path).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    
    @Tool(name = "删除文件", description = "通过路径删除文件", returnDirect = true)
    public Boolean deleteFile(@ToolParam(description = "路径") String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }
    
    @Tool(name = "写入文件", description = "将内容写入指定路径的文件")
    public Boolean writeFile(@ToolParam(description = "路径") String path, @ToolParam(description = "内容", required = false) String content) {
        FileUtil.writeBytes(content.getBytes(StandardCharsets.UTF_8), path);
        return true;
    }
    
    @Tool(name = "读文件", description = "读取指定路径的文件内容")
    public String readFile(@ToolParam(description = "路径") String path) {
        return FileUtil.readString(path, StandardCharsets.UTF_8);
    }
    
}
