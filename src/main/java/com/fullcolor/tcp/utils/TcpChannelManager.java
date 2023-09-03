package com.fullcolor.tcp.utils;

import com.fullcolor.tcp.listener.DeviceSessionAware;
import com.fullcolor.tcp.msg.resp.InstructRespData;
import com.fullcolor.tcp.msg.support.InstructData;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * tcp channel 的管理
 * 使用登陆中的pid 做唯一的key
 *
 * @author wang xiao
 * date 2023/8/9
 */
@Component
public class TcpChannelManager implements DeviceSessionAware {

    public static final AttributeKey<String> PID = AttributeKey.newInstance("pid");
    private final Logger logger = LoggerFactory.getLogger(TcpChannelManager.class);
    private final ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private final Map<String, ChannelId> channelIdMap = new ConcurrentHashMap<>();
    private final ChannelFutureListener remover = future -> {
        String pid = future.channel().attr(PID).get();
        if (channelIdMap.get(pid) == future.channel().id()) {
            channelIdMap.remove(pid);
        }
    };

    @Override
    public void awareDateMsg(String pid, InstructData instructData, Consumer<Boolean> callback) {
        logger.info("writeDateMsg:{}", instructData);
        Channel tcpChannel = get(pid);
        if (Objects.isNull(tcpChannel)) {
            callback.accept(false);
        }
        InstructRespData respData = new InstructRespData();
        BeanUtils.copyProperties(instructData, respData);
        ChannelFuture channelFuture = tcpChannel.writeAndFlush(respData);
        channelFuture.addListener(ret -> {
            if (ret.cause() == null) {
                callback.accept(true);
            } else {
                callback.accept(true);
            }
        });
    }

    public boolean add(String pid, Channel channel) {
        boolean added = channelGroup.add(channel);
        if (added) {
            if (channelIdMap.containsKey(pid)) {
                Channel old = get(pid);
                old.closeFuture().removeListener(remover);
                old.close();
            }
            channel.attr(PID).set(pid);
            channel.closeFuture().addListener(remover);
            channelIdMap.put(pid, channel.id());
        }
        return added;
    }


    public boolean remove(String pid) {
        return channelGroup.remove(channelIdMap.remove(pid));
    }

    public Channel get(String pid) {
        return channelGroup.find(channelIdMap.get(pid));
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }

}
