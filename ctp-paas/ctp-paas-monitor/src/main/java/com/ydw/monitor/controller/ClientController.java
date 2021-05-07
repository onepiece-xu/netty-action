package com.ydw.monitor.controller;

import com.ydw.monitor.model.vo.DeviceInfo;
import com.ydw.monitor.model.vo.ResultInfo;
import com.ydw.monitor.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1617:25
 */
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private IClientService clientService;

    @GetMapping("/getAllClient")
    public ResultInfo getAllClient(){
        List<DeviceInfo> allClient = clientService.getAllClient();
        return ResultInfo.success(allClient);
    }

    @GetMapping("/reportClient")
    public ResultInfo reportClient(@RequestParam String macAddr){
        clientService.reportClient(macAddr);
        return ResultInfo.success();
    }
}
