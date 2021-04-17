package com.ydw.monitor.service.impl;

import com.ydw.monitor.model.vo.DeviceInfo;
import com.ydw.monitor.model.vo.ResultInfo;
import com.ydw.monitor.service.IDeviceService;
import org.springframework.stereotype.Service;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:08
 */
@Service
public class ArmDeviceServiceImpl implements IDeviceService {

    /**
     * 重启
     * @param param
     * @return
     */
    @Override
    public ResultInfo reboot(DeviceInfo param) {
        return null;
    }

    /**
     * 重置
     * @param param
     * @return
     */
    @Override
    public ResultInfo rebuild(DeviceInfo param) {
        return null;
    }

    /**
     * 关机
     * @param param
     * @return
     */
    @Override
    public ResultInfo shutdown(DeviceInfo param) {
        return null;
    }

    /**
     * 开机
     * @param param
     * @return
     */
    @Override
    public ResultInfo open(DeviceInfo param) {
        return null;
    }

    /**
     * 初始化
     * @param param
     * @return
     */
    @Override
    public ResultInfo init(DeviceInfo param) {
        return null;
    }
}
