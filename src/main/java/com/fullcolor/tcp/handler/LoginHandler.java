package com.fullcolor.tcp.handler;

import com.fullcolor.tcp.msg.req.LoginMsg;
import com.fullcolor.tcp.msg.resp.LoginRespData;
import com.fullcolor.tcp.msg.support.InstructData;
import com.fullcolor.tcp.utils.TcpChannelManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 登陆处理
 *
 * @author wang xiao
 * date 2023/9/2
 */
@Component
@ChannelHandler.Sharable
public class LoginHandler extends BaseMsgHandler<LoginMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LoginMsg loginMsg) {
        InstructData loginData = loginMsg.getLoginData();
        Map<String, Object> loginParams = loginData.getInput();
        LoginRespData loginResp = new LoginRespData();
        Map<String, String> output = new HashMap<>(6);
        loginResp.setName("Login");
        output.put("delay", "30");
        loginResp.setOutput(output);
        if (Objects.isNull(loginParams) || loginParams.isEmpty()) {
            output.put("action", "retry");
            channelHandlerContext.writeAndFlush(loginResp);
            return;
        }
        String pid = String.valueOf(loginParams.get("pid"));
        if (StringUtils.isEmpty(pid)) {
            output.put("action", "retry");
            channelHandlerContext.writeAndFlush(loginResp);
            return;
        }
        // 允许登陆
        channelManager.add(pid, channelHandlerContext.channel());
        output.put("action", "auth");
        loginResp.setOutput(output);
        channelHandlerContext.channel().attr(TcpChannelManager.PID).set(pid);
        channelHandlerContext.writeAndFlush(loginResp);
    }
}
