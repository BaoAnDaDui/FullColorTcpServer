package com.fullcolor.endpoint.service;

import com.fullcolor.tcp.msg.support.InstructData;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 设备信息
 * @author wang xiao
 * date 2023/9/3
 */
@Component
public class DeviceService {


    /**
     * 先用map
     */
    private final Map<String, List<InstructData>> instructionResp = new HashMap<>(16);


    /**
     * 上报指令反馈结果
     *
     * @param deviceId     设备id
     * @param instructData 指令数据
     */
    public void reportInstructionResp(String deviceId, InstructData instructData) {
        List<InstructData> history = instructionResp.computeIfAbsent(deviceId, s -> new ArrayList<>(1000));
        history.add(instructData);
    }


    /**
     * 获取
     *
     * @param deviceId 设备pid
     * @return List<InstructData>
     */
    public List<InstructData> getInstructionResp(String deviceId) {
        return Collections.unmodifiableList(instructionResp.getOrDefault(deviceId,Collections.emptyList()));
    }
}
