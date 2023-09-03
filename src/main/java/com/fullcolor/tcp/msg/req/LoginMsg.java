package com.fullcolor.tcp.msg.req;

import com.fullcolor.tcp.msg.BaseMsg;
import com.fullcolor.tcp.msg.PacketHeader;
import com.fullcolor.tcp.msg.support.InstructData;

/**
 * 登陆包
 *
 * @author wang xiao
 * date 2023/9/2
 */
public class LoginMsg extends BaseMsg {

    private final InstructData loginData;

    public LoginMsg(PacketHeader header, InstructData loginData) {
        super(header);
        this.loginData = loginData;
    }

    public InstructData getLoginData() {
        return loginData;
    }
}
