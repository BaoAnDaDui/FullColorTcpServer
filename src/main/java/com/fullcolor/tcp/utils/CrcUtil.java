package com.fullcolor.tcp.utils;

import io.netty.buffer.ByteBuf;

/**
 * 数据校验
 *
 * @author wang xiao
 * date 2023/8/9
 */
public class CrcUtil {

    /**
     * 根据byteBuf的readerIndex和writerIndex计算校验码
     * 校验码规则：从消息头开始，同后一字节异或，直到校验码前一个字节，占用 1 个字节
     *
     * @param byteBuf byte
     * @return byte
     */
    public static byte xorSumByteByf(ByteBuf byteBuf) {
        byte sum = byteBuf.getByte(byteBuf.readerIndex());
        for (int i = byteBuf.readerIndex() + 1; i < byteBuf.writerIndex(); i++) {
            sum = (byte) (sum ^ byteBuf.getByte(i));
        }
        return sum;
    }

    /**
     * @param bytes byte[]
     * @return byte
     */
    public static byte xorSumBytes(byte[] bytes) {
        byte sum = bytes[0];
        for (int i = 1; i < bytes.length; i++) {
            sum = (byte) (sum ^ bytes[i]);
        }
        return sum;
    }
}
