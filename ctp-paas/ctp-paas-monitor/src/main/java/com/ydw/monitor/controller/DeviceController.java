package com.ydw.monitor.controller;

import com.ydw.monitor.model.constant.Constants;
import com.ydw.monitor.model.vo.*;
import com.ydw.monitor.service.IDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 设备处理
 * @author xulh
 *
 */
@RestController
@RequestMapping("/device")
public class DeviceController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "armDeviceServiceImpl")
    private IDeviceService armDeviceServiceImpl;
    
    @Resource(name = "pcDeviceServiceImpl")
    private IDeviceService pcDeviceServiceImpl;

    //重启
    @PostMapping(value = "/reboot")
    public ResultInfo reboot(@RequestBody DeviceInfo param) {
        if (param.getDeviceType() == Constants.DEVICE_ARCH_ARM){
        	return armDeviceServiceImpl.reboot(param);
        } else {
        	return pcDeviceServiceImpl.reboot(param);
        }
    }

    //重置
    @PostMapping(value = "/rebuild")
    public ResultInfo rebuild(@RequestBody DeviceInfo param) {
    	if (param.getDeviceType() == Constants.DEVICE_ARCH_ARM){
        	return armDeviceServiceImpl.rebuild(param);
        } else {
        	return pcDeviceServiceImpl.rebuild(param);
        }
    }

    //关机
    @PostMapping(value = "/shutdown")
    public ResultInfo shutdown(@RequestBody DeviceInfo param) {
    	if (param.getDeviceType() == Constants.DEVICE_ARCH_ARM){
        	return armDeviceServiceImpl.shutdown(param);
        } else {
        	return pcDeviceServiceImpl.shutdown(param);
        }
    }

    //开机
    @PostMapping(value = "/open")
    public ResultInfo open(@RequestBody DeviceInfo param) {
    	if (param.getDeviceType() == Constants.DEVICE_ARCH_ARM){
        	return armDeviceServiceImpl.open(param);
        } else {
        	return pcDeviceServiceImpl.open(param);
        }
    }

    
    //设置初始化设备信息
    @PostMapping(value = "/init")
    public ResultInfo init(@RequestBody DeviceInfo param){
    	if (param.getDeviceType() == Constants.DEVICE_ARCH_ARM){
        	return armDeviceServiceImpl.init(param);
        } else {
        	return pcDeviceServiceImpl.init(param);
        }
    }
}

