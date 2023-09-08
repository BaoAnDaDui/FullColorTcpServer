package com.fullcolor.tcp.msg.resp;

import com.alibaba.fastjson2.annotation.JSONField;

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
    @JSONField(serialize=false)
    default byte getEncryptType() {
        return (byte) 1;
    }

    /**
     * 加密种子
     *
     * @return byte
     */
    @JSONField(serialize=false)
    default byte getEncryptSpeed() {
        return (byte) 1;
    }

    /**
     * 获取 序列号
     *
     * @return
     */
    @JSONField(serialize=false)
    int getMessageSequence();
}
