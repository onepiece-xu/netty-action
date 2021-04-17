package com.ydw.control.service;

import com.ydw.control.model.db.App;
import com.ydw.control.model.db.Device;
import com.ydw.control.model.vo.AppInfo;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.model.vo.StreamInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用监控服务
 * @author xulh
 * @since 2021-04-14
 */
@FeignClient(value = "ctp-paas-monitor", path = "/ctp-paas-monitor")
public interface IMonitorService{
    //重启设备
    @PostMapping("/device/reboot")
    ResultInfo reboot(@RequestBody Device device);

    //重启设备
    @PostMapping("/device/shutdown")
    ResultInfo shutdown(@RequestBody Device device);

    //启动app
    @PostMapping("/app/startApp")
    ResultInfo startApp(@RequestBody AppInfo app);

    //关闭app
    @PostMapping("/app/stopApp")
    ResultInfo stopApp(@RequestBody AppInfo app);

    //开流服务
    @PostMapping("/stream/startStream")
    ResultInfo startStream(@RequestBody StreamInfo streamInfo);

    //关流服务
    @PostMapping("/stream/stopStream")
    ResultInfo stopStream(@RequestBody StreamInfo streamInfo);
}
