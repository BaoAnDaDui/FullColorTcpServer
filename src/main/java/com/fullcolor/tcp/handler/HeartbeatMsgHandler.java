package com.fullcolor.tcp.handler;

import com.fullcolor.tcp.msg.req.HeartbeatMsg;
import com.fullcolor.tcp.utils.TcpChannelManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * 心跳处理
 *
 * @author wang xiao
 * date 2023/9/2
 */
@Component
@ChannelHandler.Sharable
public class HeartbeatMsgHandler extends BaseMsgHandler<HeartbeatMsg> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, HeartbeatMsg heartbeatMsg) throws Exception {
        String pid = channelHandlerContext.channel().attr(TcpChannelManager.PID).get();
        logger.info("device pid :[{}] is Heartbeat", pid);
    }
}
