package com.ydw.control.service.impl;

import com.ydw.control.model.db.App;
import com.ydw.control.model.db.Device;
import com.ydw.control.model.db.Meterage;
import com.ydw.control.model.vo.DeviceInfo;
import com.ydw.control.model.vo.DeviceStatus;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Autowired
    private IAppService appService;

    /**
     * 注册上报
     * @param deviceInfo
     * @return
     */
    @Override
    public ResultInfo register(DeviceInfo deviceInfo) {
        logger.info("收到检测程序的注册上报！{}", deviceInfo);
        Meterage meterage = meterageService.getUnCompleteMeterageByMac(deviceInfo.getMacAddr());
        if (meterage != null){
            meterageService.endMeterage(meterage);
        }
        deviceService.addDevice(deviceInfo);
        return ResultInfo.success();
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
        Device device = deviceService.offlineDevice(macAddr);
        if (device != null){
            Meterage meterage = meterageService.getUnCompleteMeterageByMac(macAddr);
            if (meterage != null){
                logger.info("设备{}的离线上报时有计量数据{}", macAddr, meterage.getId());
                meterageService.endMeterage(meterage);
                connectLogService.addLog(meterage.getDeviceId(), meterage.getId(), 0 , null, null);
            }else{
                connectLogService.addLog(device.getId(), null, 0 , null, null);
            }
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
        Device device = deviceService.getDeviceByMac(macAddr);
        if (device.getStatus() != deviceStatus.getAgentStatus()){
            device.setStatus(deviceStatus.getAgentStatus());
            deviceService.updateById(device);
        }
        //查看是否当前是否有计量
        Meterage meterage = meterageService.getUnCompleteMeterageById(device.getId());
        if (deviceStatus.getAppStatus() == 0){
            //如果有计量，则结束计量
            if (meterage != null){
                logger.info("设备{}的app离线上报时有计量数据{},结束计量", macAddr, meterage.getId());
                meterageService.endMeterage(meterage);
            }
            //如果app未启动，则启动app
            App app = appService.list().get(0);
            logger.info("设备{}的开始打开app！", macAddr);
            appService.startApp(device, app);
        }else{
            //如果app已启动，但是没有计量则开始计量
            if (meterage == null){
                logger.info("设备{}的app上线上报时无计量数据，开始计量", macAddr);
                App app = appService.list().get(0);
                meterage = meterageService.beginMeterage(device.getId(),app.getId(),"0");
            }
        }
        connectLogService.addLog(device.getId(), meterage == null ? null : meterage.getId(), deviceStatus.getAgentStatus() , deviceStatus.getAppStatus(),
                deviceStatus.getStreamStatus());
        return ResultInfo.success();
    }
}
