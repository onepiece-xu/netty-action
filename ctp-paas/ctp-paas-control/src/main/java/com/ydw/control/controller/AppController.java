package com.ydw.control.controller;

import com.ydw.control.model.vo.AppInfo;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IAppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 游戏处理
 * @author xulh
 *
 */
@RestController
@RequestMapping("/app")
public class AppController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IAppService appService;

    //开启游戏
    @PostMapping(value = "/startApp")
    public ResultInfo startApp(@RequestParam(required = false) String appId) {
        return appService.startApp(appId);
    }

    //关闭游戏
    @PostMapping(value = "/stopApp")
    public ResultInfo stopApp(@RequestParam(required = false) String appId) {
        return appService.stopApp(appId);
    }

    //安装应用
    @PostMapping(value = "/install")
    public ResultInfo install(@RequestBody AppInfo param) {
    	return ResultInfo.success();
    }

    //卸载应用
    @PostMapping(value = "/uninstall")
    public ResultInfo uninstall(@RequestBody AppInfo param) {
        return ResultInfo.success();
    }
}

