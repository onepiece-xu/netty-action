package com.ydw.monitor.controller;

import com.ydw.monitor.model.constant.Constants;
import com.ydw.monitor.model.vo.ResultInfo;
import com.ydw.monitor.model.vo.StreamInfo;
import com.ydw.monitor.service.IStreamService;
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
@RequestMapping("/stream")
public class StreamController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource(name = "armStreamServiceImpl")
    private IStreamService armStreamServiceImpl;
    
    @Resource(name = "pcStreamServiceImpl")
    private IStreamService pcStreamServiceImpl;

    //开启流服务
    @PostMapping(value = "/startStream")
    public ResultInfo startStream(@RequestBody StreamInfo param) {
        if (param.getDeviceType() == Constants.DEVICE_ARCH_ARM){
            return armStreamServiceImpl.startStream(param);
        } else {
            return pcStreamServiceImpl.startStream(param);
        }
    }

    //关闭流服务
    @PostMapping(value = "/stopStream")
    public ResultInfo stopStream(@RequestBody StreamInfo param) {
    	if (param.getDeviceType() == Constants.DEVICE_ARCH_ARM){
        	return armStreamServiceImpl.stopStream(param);
        } else {
        	return pcStreamServiceImpl.stopStream(param);
        }
    }

}

