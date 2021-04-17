package com.ydw.control.controller;

import com.ydw.control.model.vo.DeviceInfo;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 设备处理
 * @author xulh
 *
 */
@RestController
@RequestMapping("/device")
public class DeviceController extends BaseController{

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDeviceService deviceService;

    //重启
    @PostMapping(value = "/reboot")
    public ResultInfo reboot(@RequestBody String[] devIds) {
        return deviceService.reboot(devIds);
    }

    //重置
    @PostMapping(value = "/rebuild")
    public ResultInfo rebuild(@RequestBody String[] devIds) {
    	return ResultInfo.success();
    }

    //关机
    @PostMapping(value = "/shutdown")
    public ResultInfo shutdown(@RequestBody String[] devIds) {
        return deviceService.shutdown(devIds);
    }

    //开机
    @PostMapping(value = "/open")
    public ResultInfo open(@RequestBody String[] devIds) {
        return ResultInfo.success();
    }

    //设置初始化设备信息
    @PostMapping(value = "/init")
    public ResultInfo init(@RequestBody DeviceInfo param){
        return ResultInfo.success();
    }

    //获取设备列表
    @GetMapping("/getDeviceList")
    public ResultInfo getDeviceList(@RequestParam(required = false) String search,
                                    @RequestParam(required = false) Integer status){
        return deviceService.getDeviceList(search, status, super.buildPage());
    }
}

