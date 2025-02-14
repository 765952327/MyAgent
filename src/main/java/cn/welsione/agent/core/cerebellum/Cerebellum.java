package cn.welsione.agent.core.cerebellum;

import cn.welsione.agent.core.cerebellum.instinct.Instinct;

/**
 * 小脑
 * 模仿人类小脑的功能，执行一些本能的操作
 */
public interface Cerebellum {
    void execute(String message);
}
