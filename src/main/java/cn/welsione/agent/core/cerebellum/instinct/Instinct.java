package cn.welsione.agent.core.cerebellum.instinct;

/**
 * 本能
 * 区别于思考，本能需要快速响应。
 */
public interface Instinct {
    String name();
    String description();
    String[] instructions();
    String[] params();
    void execute(String[] args);
}
