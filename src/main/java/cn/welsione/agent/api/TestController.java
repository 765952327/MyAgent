package cn.welsione.agent.api;

import cn.welsione.agent.core.cerebellum.SimpleCerebellum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {
    @Autowired
    private SimpleCerebellum cerebellum;
    
    @GetMapping("/chat")
    public void chat(String msg) {
        cerebellum.execute(msg);
    }
}
