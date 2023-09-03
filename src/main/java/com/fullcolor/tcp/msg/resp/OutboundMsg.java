package com.fullcolor.tcp.msg.resp;

/**
 * tcp server
 *
 * @author wang xiao
 * date 2023/9/3
 */
public interface OutboundMsg {

    /**
     * 加密方式 默认 不加密
     *
     * @return byte
     */
    default byte getEncryptType() {
        return (byte) 150;
    }

    /**
     * 加密种子
     *
     * @return byte
     */
    default byte getEncryptSpeed() {
        return (byte) 1;
    }

    /**
     * 获取 序列号
     *
     * @return
     */
    short getMessageSequence();
}
