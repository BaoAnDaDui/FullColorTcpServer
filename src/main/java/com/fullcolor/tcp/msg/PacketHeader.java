package com.fullcolor.tcp.msg;

import com.fullcolor.tcp.msg.resp.OutboundMsg;

/**
 * 包头
 *
 * @author wang xiao
 * date 2023/9/2
 */
public class PacketHeader implements OutboundMsg {

    /**
     * 固定头
     */
    private String magic;

    /**
     * 长度
     */
    private int frameLength;


    /**
     * 协议版本
     */
    private byte protocolVersion;


    /**
     * 协议类型
     */
    private byte protocolType;

    /**
     * 加密类型
     */
    private byte encryptionType;

    /**
     * 加密种子
     */
    private byte encryptionSeed;

    /**
     * 未加密指令部分长度
     */
    private int textLength;


    /**
     * 未加密二进制数据部分长度
     */
    private int binaryLength;

    /**
     * 保留
     */
    private Long reserved;


    /**
     * 消息id
     */
    private short messageSequence;


    /**
     * 为指令部分和二进制数据区加密前的各字节异或值
     */
    private byte dataChecksum;


    /**
     * 为报文头部前 31 字节的异或值
     */
    private byte headerChecksum;


    public String getMagic() {
        return magic;
    }

    public void setMagic(String magic) {
        this.magic = magic;
    }

    public int getFrameLength() {
        return frameLength;
    }

    public void setFrameLength(int frameLength) {
        this.frameLength = frameLength;
    }

    public byte getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(byte protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public byte getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(byte protocolType) {
        this.protocolType = protocolType;
    }

    public byte getEncryptionType() {
        return encryptionType;
    }

    public void setEncryptionType(byte encryptionType) {
        this.encryptionType = encryptionType;
    }

    public byte getEncryptionSeed() {
        return encryptionSeed;
    }

    public void setEncryptionSeed(byte encryptionSeed) {
        this.encryptionSeed = encryptionSeed;
    }

    public int getTextLength() {
        return textLength;
    }

    public void setTextLength(int textLength) {
        this.textLength = textLength;
    }

    public int getBinaryLength() {
        return binaryLength;
    }

    public void setBinaryLength(int binaryLength) {
        this.binaryLength = binaryLength;
    }

    public Long getReserved() {
        return reserved;
    }

    public void setReserved(Long reserved) {
        this.reserved = reserved;
    }

    public short getMessageSequence() {
        return messageSequence;
    }

    public void setMessageSequence(short messageSequence) {
        this.messageSequence = messageSequence;
    }

    public byte getDataChecksum() {
        return dataChecksum;
    }

    public void setDataChecksum(byte dataChecksum) {
        this.dataChecksum = dataChecksum;
    }

    public byte getHeaderChecksum() {
        return headerChecksum;
    }

    public void setHeaderChecksum(byte headerChecksum) {
        this.headerChecksum = headerChecksum;
    }
}
