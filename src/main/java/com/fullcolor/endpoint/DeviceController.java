package com.fullcolor.endpoint;

import com.fullcolor.endpoint.service.DeviceService;
import com.fullcolor.tcp.listener.DeviceSessionAware;
import com.fullcolor.tcp.msg.support.InstructData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author wang xiao
 * date 2023/9/3
 */
@RestController
@RequestMapping("/device")
public class DeviceController {


    private final DeviceSessionAware deviceSessionAware;

    private final DeviceService deviceService;

    private final Logger logger = LoggerFactory.getLogger(DeviceController.class);


    public DeviceController(DeviceSessionAware deviceSessionAware, DeviceService deviceService) {
        this.deviceSessionAware = deviceSessionAware;
        this.deviceService = deviceService;
    }

    /**
     * 向 设备发送指令
     *
     * @param instructData 指令数据 必须按照 文档填写参数
     * @param deviceId     设备id
     * @return DeferredResult 延迟结果，大概2秒 返回bool，t 代表发送成功，f 代表发送失败 超时
     */
    @PostMapping("/instruct/{deviceId}")
    public DeferredResult<ResponseEntity<Boolean>> deviceInstruct(@RequestBody InstructData instructData, @PathVariable String deviceId) {
        DeferredResult<ResponseEntity<Boolean>> deferredResult =
                new DeferredResult<>(6000L, new ResponseEntity<>(HttpStatus.OK));
        deferredResult.onTimeout(() -> {
            logger.info("发送到设备:[{}],指令:{}调用超时", deviceId, instructData);
            deferredResult.setResult(new ResponseEntity<>(false, HttpStatus.OK));
        });
        deviceSessionAware.awareDateMsg(deviceId, instructData, aBoolean -> deferredResult.setErrorResult(new ResponseEntity<>(aBoolean, HttpStatus.OK)));
        return deferredResult;
    }

    /**
     * 获取设备
     *
     * @param deviceId 设备pid
     * @return List<InstructData>
     */
    @GetMapping("/instruct/{deviceId}")
    public Collection<InstructData> getDeviceInstructions(@PathVariable String deviceId) {
        List<InstructData> list =deviceService.getInstructionResp(deviceId);
        Collections.reverse(list);
        return list;
    }

}
