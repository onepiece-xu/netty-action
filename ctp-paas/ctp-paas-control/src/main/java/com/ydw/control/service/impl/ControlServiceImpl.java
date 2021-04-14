package com.ydw.control.service.impl;

import com.ydw.control.model.db.Device;
import com.ydw.control.model.db.Meterage;
import com.ydw.control.model.vo.DeviceInfo;
import com.ydw.control.model.vo.DeviceStatus;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IControlService;
import com.ydw.control.service.IDeviceService;
import com.ydw.control.service.IConnectLogService;
import com.ydw.control.service.IMeterageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1416:27
 */
@Service
public class ControlServiceImpl implements IControlService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IMeterageService meterageService;

    @Autowired
    private IConnectLogService connectLogService;

    /**
     * 注册上报
     * @param deviceInfo
     * @return
     */
    @Override
    public ResultInfo register(DeviceInfo deviceInfo) {
        logger.info("收到检测程序的注册上报！{}", deviceInfo);
        return deviceService.addDevice(deviceInfo);
    }

    /**
     * 离线上报
     *
     * @param macAddr
     * @return
     */
    @Override
    public ResultInfo offlined(String macAddr) {
        logger.info("收到检测程序设备{}的离线上报！", macAddr);
        deviceService.offlineDevice(macAddr);
        Meterage meterage = meterageService.getUnCompleteMeterageByMac(macAddr);
        if (meterage != null){
            logger.info("设备{}的离线上报时有计量数据{}", macAddr, meterage.getId());
            LocalDateTime endTime = LocalDateTime.now();
            meterage.setEndTime(endTime);
            meterage.setTotalTime((int)Duration.between(meterage.getBeginTime(), endTime).get(ChronoUnit.MINUTES));
            meterageService.updateById(meterage);
            connectLogService.addLog(meterage.getDeviceId(), meterage.getId(), 0 , null, null);
        }else{
            Device device = deviceService.getDeviceByMac(macAddr);
            connectLogService.addLog(device.getId(), null, 0 , null, null);
        }
        return ResultInfo.success();
    }

    /**
     * 状态发生改变上报
     *
     * @param deviceStatus
     * @return
     */
    @Override
    public ResultInfo statusChanged(DeviceStatus deviceStatus) {
        logger.info("收到检测程序设备{}的状态变更上报！", deviceStatus);
        String macAddr = deviceStatus.getMacAddr();
        if (deviceStatus.getAgentStatus() == 0){
            return offlined(macAddr);
        }
        //app停止后停止计量
        Meterage meterage = meterageService.getUnCompleteMeterageByMac(macAddr);
        if (meterage != null){
            if (deviceStatus.getAppStatus() == 0){
                logger.info("设备{}的离线上报时有计量数据{}", macAddr, meterage.getId());
                LocalDateTime endTime = LocalDateTime.now();
                meterage.setEndTime(endTime);
                meterage.setTotalTime((int)Duration.between(meterage.getBeginTime(), endTime).get(ChronoUnit.MINUTES));
                meterageService.updateById(meterage);
                connectLogService.addLog(meterage.getDeviceId(), meterage.getId(), deviceStatus.getAgentStatus() , deviceStatus.getAppStatus(),
                        deviceStatus.getStreamStatus());
                return ResultInfo.success();
            }
        }
        Device device = deviceService.getDeviceByMac(macAddr);
        connectLogService.addLog(device.getId(), null, deviceStatus.getAgentStatus() , deviceStatus.getAppStatus(),
                deviceStatus.getStreamStatus());
        return ResultInfo.success();
    }
}
