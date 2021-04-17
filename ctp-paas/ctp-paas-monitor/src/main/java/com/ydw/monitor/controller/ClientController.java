package com.ydw.monitor.controller;

import com.ydw.monitor.model.vo.ResultInfo;
import com.ydw.monitor.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Map<String, String> allClient = clientService.getAllClient();
        return ResultInfo.success(allClient);
    }
}
