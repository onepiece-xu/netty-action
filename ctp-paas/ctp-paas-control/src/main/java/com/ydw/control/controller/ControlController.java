package com.ydw.control.controller;

import com.ydw.control.model.vo.DeviceInfo;
import com.ydw.control.model.vo.DeviceStatus;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IControlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xulh
 * @description: 内部监控设备上报处理类
 * @date 2021/4/1414:27
 */
@RestController
@RequestMapping("/control")
public class ControlController {

    @Autowired
    private IControlService controlService;

    /**
     * 注册上报
     * @param deviceInfo
     * @return
     */
    @PostMapping("/register")
    public ResultInfo register(@RequestBody DeviceInfo deviceInfo){
        return controlService.register(deviceInfo);
    }

    /**
     * 离线上报
     * @param macAddr
     * @return
     */
    @PostMapping("/offlined")
    public ResultInfo offlined(@RequestParam String macAddr){
        return controlService.offlined(macAddr);
    }

    /**
     * 状态发生改变上报
     * @param deviceStatus
     * @return
     */
    @PostMapping("/statusChanged")
    public ResultInfo statusChanged(@RequestBody DeviceStatus deviceStatus){
        return controlService.statusChanged(deviceStatus);
    }
}
