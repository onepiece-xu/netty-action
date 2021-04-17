package com.ydw.control.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    Device addDevice(DeviceInfo param);

    /**
     * 设备掉线
     * @param macAddr
     * @return
     */
    Device offlineDevice(String macAddr);

    /**
     * 根据mac地址获取设备
     * @param macAddr
     * @return
     */
    Device getDeviceByMac(String macAddr);

    /**
     * 获取设备列表
     * @param search
     * @param status
     * @param buildPage
     * @return
     */
    ResultInfo getDeviceList(String search, Integer status, Page buildPage);
}
