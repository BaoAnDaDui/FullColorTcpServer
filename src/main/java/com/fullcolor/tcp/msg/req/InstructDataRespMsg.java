package com.fullcolor.tcp.msg.req;

import com.fullcolor.tcp.msg.BaseMsg;
import com.fullcolor.tcp.msg.PacketHeader;
import com.fullcolor.tcp.msg.support.InstructData;

/**
 * 设备发送的指令回复 所以是resp
 *
 * @author wang xiao
 * date 2023/9/3
 */
public class InstructDataRespMsg extends BaseMsg {

    private final InstructData instructData;

    public InstructDataRespMsg(PacketHeader header, InstructData instructData) {
        super(header);
        this.instructData = instructData;
    }

    public InstructData getInstructData() {
        return instructData;
    }
}
