package cn.welsione.agent.command;

import cn.welsione.agent.core.ai.AssistedAI;
import java.util.Scanner;
import java.util.UUID;
import org.fusesource.jansi.Ansi;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import reactor.core.publisher.Flux;

@ShellComponent
public class AgentCommands {
    @Autowired
    private AssistedAI client;
    
    @ShellMethod(key = "chat", value = "Chat with the agent")
    public void chat() {
        String conversationId = UUID.randomUUID().toString();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your message,`exit` to quit:");
        while (true) {
            String input = scanner.nextLine();
            // 检查是否退出
            if ("exit".equalsIgnoreCase(input)) {;
                break;
            }
            Flux<String> flux = client.streamCall(conversationId, new Prompt(input));
            flux.blockFirst();
            flux.subscribe(
                    chunk -> System.out.print(Ansi.ansi().fgDefault().a(chunk).reset()),
                    error -> System.out.println(Ansi.ansi().fgRed().a("Error: " + error).reset()),
                    () -> System.out.println("\n")
            );
        }
    }
}
