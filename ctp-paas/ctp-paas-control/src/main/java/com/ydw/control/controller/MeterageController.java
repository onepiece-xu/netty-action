package com.ydw.control.controller;


import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IMeterageService;
import com.ydw.control.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 计量处理
 * @author xulh
 * @since 2021-04-14
 */
@RestController
@RequestMapping("/meterage")
public class MeterageController extends BaseController{

    @Autowired
    private IMeterageService meterageService;

    @Autowired
    private IUserService userService;

    //获取计量列表
    @GetMapping("/getMeterageList")
    public ResultInfo getMeterageList(@RequestParam(required = false) String search,
                                      @RequestParam(required = false) Integer status,
                                    @RequestParam(required = false) String beginDate,
                                    @RequestParam(required = false) String endDate){
        List<String> clusterIds = userService.getClusterIds(super.getToken());
        return meterageService.getMeterageList(clusterIds, search, status, beginDate, endDate, super.buildPage());
    }

    //获取计量统计
    @GetMapping("/getMeterageCount")
    public ResultInfo getMeterageCount(@RequestParam(required = false) String search,
                                      @RequestParam(required = false) Integer status,
                                      @RequestParam(required = false) String beginDate,
                                      @RequestParam(required = false) String endDate){
        List<String> clusterIds = userService.getClusterIds(super.getToken());
        return meterageService.getMeterageCount(clusterIds, search, status, beginDate, endDate);
    }
}

