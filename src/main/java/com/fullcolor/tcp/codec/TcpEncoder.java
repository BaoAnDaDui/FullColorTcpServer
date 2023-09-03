package com.fullcolor.tcp.codec;

import com.alibaba.fastjson2.JSONObject;
import com.fullcolor.tcp.msg.resp.OutboundMsg;
import com.fullcolor.tcp.utils.BytesUtil;
import com.fullcolor.tcp.utils.CrcUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * 编码
 *
 * @author wang xiao
 * date 2023/9/2
 */
public class TcpEncoder extends MessageToByteEncoder<OutboundMsg> {

    private final Logger logger = LoggerFactory.getLogger(TcpEncoder.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, OutboundMsg outboundMsg, ByteBuf byteBuf) {
        String outMsgStr = JSONObject.toJSONString(outboundMsg);
        // 原始字节数据
        byte[] sourceOutRaw = outMsgStr.getBytes(StandardCharsets.UTF_8);
        // 申请内存 开始写
        ByteBuf bb = ByteBufAllocator.DEFAULT.heapBuffer();
        // 数据进行加密
        byte encryptType = outboundMsg.getEncryptType();
        byte encryptSpeed = outboundMsg.getEncryptSpeed();
        byte[] encryptRaw = BytesUtil.encryptByteBuf(sourceOutRaw, encryptType, encryptSpeed);
        int frameLength = 32 + encryptRaw.length;
        // 开始写数据
        bb.writeBytes("BXLC".getBytes(StandardCharsets.UTF_8));
        // 报文长度
        bb.writeIntLE(frameLength);
        // 两个固定的
        bb.writeByte(7);
        bb.writeByte(3);
        // 加密
        bb.writeByte(encryptType);
        bb.writeByte(encryptSpeed);
        // 未加密 指令长度
        bb.writeIntLE(sourceOutRaw.length);
        // 二进制未加密长度
        bb.writeIntLE(0);
        // 保留 字节数据
        bb.writeLongLE(0);
        // 消息序列号
        bb.writeShortLE(outboundMsg.getMessageSequence());
        // 数据部分crc
        byte dataCrc = CrcUtil.xorSumBytes(encryptRaw);
        bb.writeByte(dataCrc);
        //头部 crc
        byte headCrc = CrcUtil.xorSumByteByf(bb);
        bb.writeByte(headCrc);
        // 指令数据
        bb.writeBytes(encryptRaw);
        logger.info("Send message to ip:[{}],msg is hex:[{}]\n", channelHandlerContext.channel().remoteAddress(), ByteBufUtil.hexDump(bb));
        byteBuf.writeBytes(bb);
        ReferenceCountUtil.safeRelease(sourceOutRaw);
        ReferenceCountUtil.safeRelease(encryptRaw);
        ReferenceCountUtil.safeRelease(bb);
    }

}
