package com.ydw.control.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.control.model.db.Device;
import com.ydw.control.model.vo.DeviceInfo;
import com.ydw.control.model.vo.ResultInfo;

public interface IDeviceService extends IService<Device> {

    /**
     * 重启
     * @param devIds
     * @return
     */
    ResultInfo reboot(String[] devIds);

    /**
     * 重置
     * @param devIds
     * @return
     */
    ResultInfo rebuild(String[] devIds);

    /**
     * 关机
     * @param devIds
     * @return
     */
    ResultInfo shutdown(String[] devIds);

    /**
     * 开机
     * @param devIds
     * @return
     */
    ResultInfo open(String[] devIds);

    /**
     * 初始化
     * @param param
     * @return
     */
	ResultInfo init(DeviceInfo param);

    /**
     * 新增设备，如果已存在就修改状态
     * @param param
     * @return
     */
    ResultInfo addDevice(DeviceInfo param);

    /**
     * 设备掉线
     * @param macAddr
     * @return
     */
    ResultInfo offlineDevice(String macAddr);

    Device getDeviceByMac(String macAddr);
}
