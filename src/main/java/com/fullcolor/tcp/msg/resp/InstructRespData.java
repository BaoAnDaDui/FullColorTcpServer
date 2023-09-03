package com.fullcolor.tcp.msg.resp;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author wang xiao
 * date 2023/9/3
 */
public class InstructRespData implements OutboundMsg {
    private String name;

    private Map<String, Object> input;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Object> getInput() {
        return input;
    }

    public void setInput(Map<String, Object> input) {
        this.input = input;
    }

    @Override
    public short getMessageSequence() {
        int randomEvenNum = ThreadLocalRandom.current().nextInt((16383)) * 2 + 1;
        return (short) randomEvenNum;
    }
}
