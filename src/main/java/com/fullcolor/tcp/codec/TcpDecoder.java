package com.fullcolor.tcp.codec;

import com.alibaba.fastjson2.JSONObject;
import com.fullcolor.tcp.msg.BaseMsg;
import com.fullcolor.tcp.msg.PacketHeader;
import com.fullcolor.tcp.msg.req.HeartbeatMsg;
import com.fullcolor.tcp.msg.req.InstructDataRespMsg;
import com.fullcolor.tcp.msg.req.LoginMsg;
import com.fullcolor.tcp.msg.support.InstructData;
import com.fullcolor.tcp.utils.BytesUtil;
import com.fullcolor.tcp.utils.CrcUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.ReferenceCountUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * 解码
 *
 * @author wang xiao
 * date 2023/9/2
 */
public class TcpDecoder extends ByteToMessageDecoder {

    private final Logger logger = LoggerFactory.getLogger(TcpDecoder.class);

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        logger.info("Received message From ip:[{}],msg is hex:[{}]", channelHandlerContext.channel().remoteAddress(), ByteBufUtil.hexDump(byteBuf));
        try {
            BaseMsg msg = decode(byteBuf);
            if (msg != null) {
                list.add(msg);
            }
        } catch (Exception e) {
            logger.error("decode is error:{}", e.getMessage());
        }
    }

    private BaseMsg decode(ByteBuf in) {
        // 头32 长度
        if (in.readableBytes() < 32) {
            logger.error("invalid inbound message,because readable byte :[{}] less than 32 bytes", in.readableBytes());
            return null;
        }
        PacketHeader packetHeader = new PacketHeader();
        // 取 31 字节头 用于crc
        byte[] header = new byte[31];
        in.readBytes(header);
        // 重置readIdx
        in.resetReaderIndex();
        // 固定头
        String magic = BytesUtil.convertByteBufToString(in.readBytes(4));
        if (!StringUtils.equals("BXLC", magic)) {
            logger.error("头部签名错误,magic :[{}]read buf from", magic);
            ReferenceCountUtil.safeRelease(header);
            ReferenceCountUtil.safeRelease(in);
            packetHeader = null;
            return null;
        }
        packetHeader.setMagic(magic);
        //报文长度
        int frameLength = in.readBytes(4).getIntLE(4);
        packetHeader.setFrameLength(frameLength);
        // 版本
        byte protocolVersion = in.readByte();
        packetHeader.setProtocolVersion(protocolVersion);
        // 类型
        byte protocolType = in.readByte();
        packetHeader.setProtocolType(protocolType);
        // 加密类型
        byte encryptionType = in.readByte();
        packetHeader.setEncryptionType(encryptionType);
        // 加密种子
        byte encryptionSeed = in.readByte();
        packetHeader.setEncryptionSeed(encryptionSeed);
        // 未加密时候 指令长度
        int textLength = in.readBytes(4).getIntLE(4);
        packetHeader.setTextLength(textLength);
        // 未加密二进制 报文长度
        int binaryLength = in.readBytes(4).getIntLE(4);
        packetHeader.setBinaryLength(binaryLength);
        // Reserved 保留8 字节0
        long reserved = in.readBytes(8).readLongLE();
        packetHeader.setReserved(reserved);
        // MessageSequence
        short messageSequence = in.readBytes(2).readShortLE();
        packetHeader.setMessageSequence(messageSequence);
        // DataChecksum
        byte dataChecksum = in.readByte();
        packetHeader.setDataChecksum(dataChecksum);
        // HeaderChecksum
        byte headerChecksum = in.readByte();
        packetHeader.setHeaderChecksum(headerChecksum);
        byte calHeaderCrc = CrcUtil.xorSumBytes(header);
        if (calHeaderCrc != headerChecksum) {
            logger.error("头部校验码错误,calHeaderCrc:[{}],headerChecksum :[{}]read buf from", calHeaderCrc, headerChecksum);
            ReferenceCountUtil.safeRelease(header);
            ReferenceCountUtil.safeRelease(in);
            return null;
        }
        // 只有头部 是心跳
        if (in.readableBytes() == 0) {
            return new HeartbeatMsg(packetHeader);
        }
        // 读取数据部分
        ByteBuf data = in.readBytes(in.readableBytes());
        // 解密后数据
        ByteBuf rawData = BytesUtil.decryptByteBuf(data, encryptionType, encryptionType);
        // 读取指令内容
        if (rawData.readableBytes() != textLength) {
            logger.error("指令部分数据解析与预期不符合,textLength:[{}],can readByteLength :[{}]read buf from", textLength, rawData.readableBytes());
        }
        String instructStr = BytesUtil.convertByteBufToString(rawData.readBytes(textLength));
        InstructData instructData = JSONObject.parseObject(instructStr, InstructData.class);
        if (Objects.isNull(instructData)) {
            return null;
        }
        if (StringUtils.equals("Login", instructData.getName())) {
            return new LoginMsg(packetHeader, instructData);
        }
        // 二进制内容  忽略
        ReferenceCountUtil.safeRelease(data);
        ReferenceCountUtil.safeRelease(rawData);
        return new InstructDataRespMsg(packetHeader, instructData);
    }

}
