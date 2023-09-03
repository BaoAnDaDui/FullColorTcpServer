package com.fullcolor.tcp.handler;

import com.fullcolor.tcp.utils.TcpChannelManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * 通用消息处理
 *
 * @author wang xiao
 * date 2023/9/2
 */
public abstract class BaseMsgHandler<T> extends SimpleChannelInboundHandler<T> {

    protected final Logger logger = LoggerFactory.getLogger(BaseMsgHandler.class);


    protected TcpChannelManager channelManager;


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("exceptionCaught", cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            logger.warn("客户端:[{}],idle why:{}", ctx.channel().remoteAddress(), state);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


    @Autowired
    public void setChannelManager(TcpChannelManager channelManager) {
        this.channelManager = channelManager;
    }
}
