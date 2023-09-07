package com.fullcolor.tcp.msg.resp;

import java.util.Map;

/**
 * 登陆反馈
 *
 * @author wang xiao
 * date 2023/9/3
 */
public class LoginRespData implements OutboundMsg {

    private String name;

    private Map<String, String> output;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getOutput() {
        return output;
    }

    public void setOutput(Map<String, String> output) {
        this.output = output;
    }

    @Override
    public int getMessageSequence() {
        return 1;
    }
}
