package com.fullcolor.tcp.utils;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;

/**
 * 字节工具
 *
 * @author wang xiao
 * date 2023/9/3
 */
public class BytesUtil {

    private BytesUtil() {
    }

    public static String convertByteBufToString(ByteBuf buf) {
        String str;
        if (buf.hasArray()) {
            str = new String(buf.array(), buf.arrayOffset() + buf.readerIndex(), buf.readableBytes());
        } else {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            str = new String(bytes, 0, buf.readableBytes());
        }
        ReferenceCountUtil.safeRelease(buf);
        return str;
    }

    private static byte[] getBytes(ByteBuf buf){
        if (buf.hasArray()) {
            return buf.array();
        } else {
            byte[] bytes = new byte[buf.readableBytes()];
            buf.getBytes(buf.readerIndex(), bytes);
            return bytes;
        }
    }

    public static byte[] encryptByteBuf(byte[] buf, byte encryptionType, byte encryptionSpeed) {
        switch (encryptionType) {
            case 1:
                return xorEncrypt(buf, encryptionSpeed);
            case 2:
                return xorEncrypt2(buf, encryptionSpeed);
            case 3:
                return xorEncrypt3(buf, encryptionSpeed);
            case 4:
                return xorEncrypt4(buf, encryptionSpeed);
            default:
                return buf;
        }

    }

    public static ByteBuf decryptByteBuf(ByteBuf buf, byte encryptionType, byte encryptionSpeed) {
        switch (encryptionType) {
            case 1:
                return xorDecrypt(buf, encryptionSpeed);
            case 2:
                return xorDecrypt2(buf, encryptionSpeed);
            case 3:
                return xorDecrypt3(buf, encryptionSpeed);
            case 4:
                return xorDecrypt4(buf, encryptionSpeed);
            default:
                return buf;
        }

    }

    /**
     * 异或 加密
     */
    private static byte[] xorEncrypt(byte[] src, byte speed) {
        byte[] dest = new byte[src.length];
        for (int i = 0; i < src.length; i++) {
            dest[i] = (byte) (src[i] ^ speed);
        }
        return dest;
    }

    /**
     * 异或 解密
     */
    private static ByteBuf xorDecrypt(ByteBuf src, byte speed) {
        ByteBuf dest = src.alloc().buffer(src.readableBytes());
        for (int i = src.readerIndex(); i < src.writerIndex(); i++) {
            dest.writeByte(src.readByte() ^ speed);
        }
        return dest;
    }

    /**
     * 异或 并且将结果作为下一字节的种子 加密
     */
    private static byte[] xorEncrypt2(byte[] src, byte speed) {
        byte[] dest = new byte[src.length];
        for (int i = 0; i < src.length; i++) {
            if (i==0){
                dest[i] = (byte) (src[i] ^ speed);
            }else {
                dest[i] = (byte) (src[i] ^ src[i-1]);
            }
        }
        return dest;
    }

    /**
     * 异或 并且将结果作为下一字节的种子 解密
     */
    private static ByteBuf xorDecrypt2(ByteBuf src, byte speed) {
        ByteBuf dest = src.alloc().buffer(src.readableBytes());
        byte[] srcArray = getBytes(src);
        for (int i = 0; i < srcArray.length; i++) {
            if (i==0){
                dest.writeByte(srcArray[i] ^ speed);
            }else {
                dest.writeByte(srcArray[i] ^ srcArray[i-1]);
            }
        }
        return dest;
    }

    /**
     * 异或 种子 左一 一位 加密
     */
    private static byte[] xorEncrypt3(byte[] src, byte speed) {
        byte[] dest = new byte[src.length];
        for (int i = 0; i < src.length; i++) {
            dest[i] = (byte) (src[i] ^ speed);
            int overflow = (speed>>7) & 0x01;
            speed = (byte) (((speed << 1) &0xff) | overflow);
        }
        return dest;
    }

    /**
     * 异或 种子 左一 一位 解密
     */
    private static ByteBuf xorDecrypt3(ByteBuf src, byte speed) {
        ByteBuf dest = src.alloc().buffer(src.readableBytes());
        for (int i = src.readerIndex(); i < src.writerIndex(); i++) {
            byte data = src.readByte();
            dest.writeByte((data ^ speed));
            int overflow = (speed>>7) & 0x01;
            speed = (byte) (((speed << 1) &0xff) | overflow);
        }
        return dest;
    }


    /**
     * 异或 种子 右一 一位 加密
     */
    private static byte[] xorEncrypt4(byte[] src, byte speed) {
        byte[] dest = new byte[src.length];
        for (int i = 0; i < src.length; i++) {
            dest[i] =(byte) (src[i] ^ speed);
            int overflow = (speed << 7) & 0x80;
            speed = (byte) (((speed >> 1) & 0x7f) | overflow);
        }
        return dest;
    }

    /**
     * 异或 种子 右一 一位 解密
     */
    private static ByteBuf xorDecrypt4(ByteBuf src, byte speed) {
        ByteBuf dest = src.alloc().buffer(src.readableBytes());
        for (int i = src.readerIndex(); i < src.writerIndex(); i++) {
            byte data = src.readByte();
            dest.writeByte(data ^ speed);
            int overflow = (speed << 7) & 0x80;
            speed = (byte) (((speed >> 1)& 0x7f)| overflow);
        }
        return dest;

    }

}
