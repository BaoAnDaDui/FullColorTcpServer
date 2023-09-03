package com.fullcolor.tcp.msg.support;

import java.util.Map;

/**
 * 指令数据
 *
 * @author wang xiao
 * date 2023/9/2
 */
public class InstructData {

    private String name;

    private Map<String, Object> input;

    /**
     * 指令数据
     */

    private ErrorData error;

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

    public ErrorData getError() {
        return error;
    }

    public void setError(ErrorData error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "InstructData{" +
                "name='" + name + '\'' +
                ", input=" + input +
                ", error=" + error +
                '}';
    }
}
