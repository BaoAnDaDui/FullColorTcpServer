package com.fullcolor.tcp;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * tcp server
 *
 * @author wang xiao
 * date 2023/9/2
 */
@Component
public class TcpServer {

    private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);

    private EventLoopGroup bossEventLoopGroup;

    private EventLoopGroup workerEventLoopGroup;

    @Value("${netty.port}")
    private int port;

    @Value("${netty.threads.boss}")
    private int bossThreadsNum;

    @Value("${netty.threads.worker}")
    private int workerThreadsNum;

    private TcpServerChannelInitializer channelInitializer;


    /**
     * 启动Server
     */
    @PostConstruct
    public void start() throws InterruptedException {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.SIMPLE);
        logger.info("TCP transport Server Starting...");
        this.bossEventLoopGroup = new NioEventLoopGroup(bossThreadsNum);
        this.workerEventLoopGroup = new NioEventLoopGroup(workerThreadsNum);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossEventLoopGroup, workerEventLoopGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitializer)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        if (channelFuture.isSuccess()) {
            logger.info("TCP transport Server Started,port={}", this.port);
        }
    }

    /**
     * 销毁资源
     */
    @PreDestroy
    public void destroy() {
        bossEventLoopGroup.shutdownGracefully().syncUninterruptibly();
        workerEventLoopGroup.shutdownGracefully().syncUninterruptibly();
        logger.info("TCP transport Server Stop...");
    }

    @Autowired
    public void setChannelInitializer(TcpServerChannelInitializer channelInitializer) {
        this.channelInitializer = channelInitializer;
    }

}
