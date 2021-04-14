package com.ydw.monitor.controller;

import com.ydw.monitor.model.constant.Constants;
import com.ydw.monitor.model.vo.AppInfo;
import com.ydw.monitor.model.vo.ResultInfo;
import com.ydw.monitor.service.IAppService;
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
@RequestMapping("/app")
public class AppController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "armAppServiceImpl")
    private IAppService armAppServiceImpl;

    @Resource(name = "pcAppServiceImpl")
    private IAppService pcAppServiceImpl;

    //开启游戏
    @PostMapping(value = "/startApp")
    public ResultInfo startApp(@RequestBody AppInfo param) {
        if (param.getDeviceType() == Constants.DEVICE_ARCH_ARM){
            return armAppServiceImpl.startApp(param);
        } else {
            return pcAppServiceImpl.startApp(param);
        }
    }

    //关闭游戏
    @PostMapping(value = "/stopApp")
    public ResultInfo stopApp(@RequestBody AppInfo param) {
        if (param.getDeviceType() == Constants.DEVICE_ARCH_ARM){
            return armAppServiceImpl.stopApp(param);
        } else {
            return pcAppServiceImpl.stopApp(param);
        }
    }

    //安装应用
    @PostMapping(value = "/install")
    public ResultInfo install(@RequestBody AppInfo param) {
    	if (param.getDeviceType() == Constants.DEVICE_ARCH_ARM){
        	return armAppServiceImpl.install(param);
        } else {
        	return pcAppServiceImpl.install(param);
        }
    }

    //卸载应用
    @PostMapping(value = "/uninstall")
    public ResultInfo uninstall(@RequestBody AppInfo param) {
    	if (param.getDeviceType() == Constants.DEVICE_ARCH_ARM){
        	return armAppServiceImpl.unInstall(param);
        } else {
        	return pcAppServiceImpl.unInstall(param);
        }
    }
}

