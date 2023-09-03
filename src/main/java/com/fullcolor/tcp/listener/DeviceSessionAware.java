package com.fullcolor.tcp.listener;

import com.fullcolor.tcp.msg.support.InstructData;

import java.util.function.Consumer;

/**
 * 设备连接 感知
 *
 * @author wang xiao
 * date 2023/9/2
 */
public interface DeviceSessionAware {


    /**
     * 感知 要发送的设备的数据
     *
     * @param pid          设备的唯一标识
     * @param instructData 指令数据
     * @param callback     回调函数，true 成功 false 失败
     */
    void awareDateMsg(String pid, InstructData instructData, Consumer<Boolean> callback);
}
