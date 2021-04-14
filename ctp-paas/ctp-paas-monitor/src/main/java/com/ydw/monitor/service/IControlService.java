package com.ydw.monitor.service;

import com.ydw.monitor.model.vo.DeviceInfo;
import com.ydw.monitor.model.vo.DeviceStatus;
import com.ydw.monitor.model.vo.ResultInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用监控服务
 * @author xulh
 * @since 2021-04-14
 */
@FeignClient(value = "ctp-paas-monitor", path = "/ctp-paas-monitor")
public interface IControlService {

    //设备注册上报
    @PostMapping("/control/register")
    ResultInfo register(@RequestBody DeviceInfo device);

    //设备离线上报
    @PostMapping("/control/offlined")
    ResultInfo offlined(@RequestParam String macAddr);

    //状态改变上报
    @PostMapping("/control/statusChanged")
    ResultInfo statusChanged(@RequestBody DeviceStatus status);

}
