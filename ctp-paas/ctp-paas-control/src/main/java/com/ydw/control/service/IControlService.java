package com.ydw.control.service;

import com.ydw.control.model.vo.DeviceInfo;
import com.ydw.control.model.vo.DeviceStatus;
import com.ydw.control.model.vo.ResultInfo;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1416:24
 */
public interface IControlService {

    /**
     * 注册上报
     * @param deviceInfo
     * @return
     */
    ResultInfo register(DeviceInfo deviceInfo);

    /**
     * 离线上报
     * @param macAddr
     * @return
     */
    ResultInfo offlined(String macAddr);

    /**
     * 状态发生改变上报
     * @param deviceStatus
     * @return
     */
    ResultInfo statusChanged(DeviceStatus deviceStatus);
}
