package com.fullcolor.tcp.msg;

/**
 * @author wang xiao
 * date 2023/9/3
 */
public abstract class BaseMsg {

    private final PacketHeader header;

    public BaseMsg(PacketHeader header) {
        this.header = header;
    }

    public PacketHeader getHeader() {
        return header;
    }

}
