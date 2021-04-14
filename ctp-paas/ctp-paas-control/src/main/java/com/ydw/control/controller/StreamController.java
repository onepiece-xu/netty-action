package com.ydw.control.controller;

import com.alibaba.fastjson.JSONObject;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.model.vo.StreamInfo;
import com.ydw.control.service.IStreamService;
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
@RequestMapping("/stream")
public class StreamController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IStreamService streamService;

    //开启流服务
    @PostMapping(value = "/connectStream")
    public ResultInfo connectStream(@RequestParam String deviceId) {
        return streamService.connectStream(deviceId);
    }

}

