package com.fullcolor.tcp;

import com.fullcolor.tcp.codec.TcpDecoder;
import com.fullcolor.tcp.codec.TcpEncoder;
import com.fullcolor.tcp.handler.HeartbeatMsgHandler;
import com.fullcolor.tcp.handler.LoginHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * channel 初始化
 *
 * @author wang xiao
 * date 2023/9/2
 */
@Component
public class TcpServerChannelInitializer extends ChannelInitializer<SocketChannel> {


    private final HeartbeatMsgHandler heartbeatHandler;

    private final LoginHandler loginHandler;

    public TcpServerChannelInitializer(HeartbeatMsgHandler heartbeatHandler, LoginHandler loginHandler) {
        this.heartbeatHandler = heartbeatHandler;
        this.loginHandler = loginHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new TcpDecoder());
        pipeline.addLast(new TcpEncoder());
        pipeline.addLast(heartbeatHandler);
        pipeline.addLast(loginHandler);
    }


}
