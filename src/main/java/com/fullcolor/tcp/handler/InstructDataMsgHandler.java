package com.fullcolor.tcp.handler;

import com.fullcolor.endpoint.service.DeviceService;
import com.fullcolor.tcp.msg.req.InstructDataRespMsg;
import com.fullcolor.tcp.msg.support.InstructData;
import com.fullcolor.tcp.utils.TcpChannelManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 指令反馈
 *
 * @author wang xiao
 * date 2023/9/3
 */
@Component
@ChannelHandler.Sharable
public class InstructDataMsgHandler extends BaseMsgHandler<InstructDataRespMsg> {


    private DeviceService deviceService;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, InstructDataRespMsg instructDataRespMsg) {
        InstructData instructDataResp = instructDataRespMsg.getInstructData();
        String pid = channelHandlerContext.channel().attr(TcpChannelManager.PID).get();
        logger.info("device pid :[{}],Received instructDataResp", pid);
        deviceService.reportInstructionResp(pid,instructDataResp);
    }


    @Autowired
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
}
