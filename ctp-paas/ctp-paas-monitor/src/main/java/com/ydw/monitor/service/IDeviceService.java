package com.ydw.monitor.service;

import com.ydw.monitor.model.vo.DeviceInfo;
import com.ydw.monitor.model.vo.ResultInfo;

public interface IDeviceService {

    /**
     * 重启
     * @param param
     * @return
     */
    ResultInfo reboot(DeviceInfo param);

    /**
     * 重置
     * @param param
     * @return
     */
    ResultInfo rebuild(DeviceInfo param);

    /**
     * 关机
     * @param param
     * @return
     */
    ResultInfo shutdown(DeviceInfo param);

    /**
     * 开机
     * @param param
     * @return
     */
    ResultInfo open(DeviceInfo param);

    /**
     * 初始化
     * @param param
     * @return
     */
	ResultInfo init(DeviceInfo param);


}
