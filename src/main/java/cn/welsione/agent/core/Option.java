package cn.welsione.agent.core;

import java.io.Serializable;

public class Option implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private String[] args;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String[] getArgs() {
        return args;
    }
    
    public void setArgs(String[] args) {
        this.args = args;
    }
}
