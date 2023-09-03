package com.fullcolor.tcp.msg.req;


import com.fullcolor.tcp.msg.BaseMsg;
import com.fullcolor.tcp.msg.PacketHeader;

/**
 * 心跳
 *
 * @author wang xiao
 * date 2023/9/2
 */
public class HeartbeatMsg extends BaseMsg {

    public HeartbeatMsg(PacketHeader header) {
        super(header);
    }

}
