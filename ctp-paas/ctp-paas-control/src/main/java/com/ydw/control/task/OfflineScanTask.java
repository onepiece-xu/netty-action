package com.ydw.control.task;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ydw.control.model.db.Device;
import com.ydw.control.model.vo.DeviceInfo;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IControlService;
import com.ydw.control.service.IDeviceService;
import com.ydw.control.service.IMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Component
public class OfflineScanTask implements StartTask{

	Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${task.offlineScan.period}")
	private Long offlineScanPeriod;

	@Autowired
    private IMonitorService monitorService;

	@Autowired
	private IDeviceService deviceService;

	@Autowired
	private IControlService controlService;

	private Timer timer = new Timer();

	@Override
	public void run() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                doScan();
            }
        }, 10*1000, offlineScanPeriod * 1000);
	}

	public void doScan(){
        logger.info("--------------------扫描检查开始---------------------");
        //获取control在线的设备
        QueryWrapper<Device> qw = new QueryWrapper<>();
        qw.eq("status", 1);
        List<Device> devices = deviceService.list(qw);
        if (devices == null || devices.isEmpty()){
            logger.info("无在线设备，本次检查跳过");
            return;
        }
        //获取monitor程序在线的设备
        List<DeviceInfo> list = new ArrayList<>();
        ResultInfo resultInfo = monitorService.getAllClient();
        if (resultInfo.getCode() == 200){
            list = JSON.parseArray(JSON.toJSONString(resultInfo.getData()),DeviceInfo.class);
        }
        for (Device device : devices) {
            String macAddr = device.getMacAddr();
            boolean active = false;
            for (DeviceInfo deviceInfo : list) {
                if (deviceInfo.getMacAddr().equals(macAddr)){
                    active = true;
                    break;
                }
            }
            if (!active){
                logger.info("在monitor检测程序中，此客户端{}已经不在线", macAddr);
                controlService.offlined(macAddr);
            }
        }
        for (DeviceInfo deviceInfo : list) {
            String macAddr = deviceInfo.getMacAddr();
            boolean active = false;
            for (Device device : devices) {
                if (device.getMacAddr().equals(macAddr)){
                    active = true;
                    break;
                }
            }
            if (!active){
                logger.info("在contorl检测程序中，此客户端{}已经不在线", macAddr);
                monitorService.reportClient(macAddr);
            }
        }
        logger.info("--------------------扫描检查结束---------------------");
    }
}
