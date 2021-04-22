package com.ydw.control.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.control.dao.DeviceMapper;
import com.ydw.control.model.db.Device;
import com.ydw.control.model.vo.DeviceInfo;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IDeviceService;
import com.ydw.control.service.IMonitorService;
import com.ydw.control.utils.SequenceGenerator;
import com.ydw.control.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:08
 */
@Service
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements IDeviceService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IMonitorService monitorService;

    @Autowired
    private DeviceMapper deviceMapper;

    /**
     * 重启
     *
     * @param devIds
     * @return
     */
    @Override
    public ResultInfo reboot(String[] devIds) {
        QueryWrapper<Device> qw = new QueryWrapper<>();
        qw.in("id", devIds);
        List<Device> list = super.list(qw);
        for (Device device : list){
            monitorService.reboot(device);
        }
        return ResultInfo.success();
    }

    /**
     * 重置
     *
     * @param devIds
     * @return
     */
    @Override
    public ResultInfo rebuild(String[] devIds) {
        return null;
    }

    /**
     * 关机
     *
     * @param devIds
     * @return
     */
    @Override
    public ResultInfo shutdown(String[] devIds) {
        QueryWrapper<Device> qw = new QueryWrapper<>();
        qw.in("id", devIds);
        List<Device> list = super.list(qw);
        for (Device device : list){
            monitorService.shutdown(device);
        }
        return ResultInfo.success();
    }

    /**
     * 开机
     *
     * @param devIds
     * @return
     */
    @Override
    public ResultInfo open(String[] devIds) {
        return null;
    }

    /**
     * 初始化
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo init(DeviceInfo param) {
        return null;
    }

    /**
     * 新增设备，如果已存在则修改状态
     * @param param
     * @return
     */
    @Override
    public Device addDevice(DeviceInfo param) {
        QueryWrapper<Device> qw = new QueryWrapper<>();
        qw.eq("mac_addr", param.getMacAddr());
        Device device = getOne(qw);
        if (device == null){
            logger.info("此设备{}为第一次注册，录入设备信息！", param);
            device = new Device();
            device.setCpuModel(param.getCpuModel());
            device.setCpuNumber(param.getCpuNumber());
            device.setDiskModel(param.getDiskModel());
            device.setDiskSize(param.getDiskSize());
            device.setGraphics(param.getGraphics());
            device.setId(SequenceGenerator.sequence());
            device.setInternetIp(param.getInternetIP());
            device.setIntranetIp(param.getIntranetIp());
            device.setMacAddr(param.getMacAddr());
            device.setMemoryModel(param.getMemoryModel());
            device.setMemorySize(param.getMemorySize());
            device.setName(param.getDeviceName());
            device.setNetworkCard(param.getNetworkCard());
            device.setOs(param.getOs());
            device.setStatus(0);
            device.setType(param.getDeviceType());
            device.setCreateTime(LocalDateTime.now());
            save(device);
        }else{
            logger.info("此设备{}已注册过！", param);
        }
        return device;
    }

    /**
     * 设备掉线
     *
     * @param macAddr
     * @return
     */
    @Override
    public Device offlineDevice(String macAddr) {
        QueryWrapper<Device> qw = new QueryWrapper<>();
        qw.eq("mac_addr", macAddr);
        Device device = getOne(qw);
        if (device == null){
            logger.error("此设备{}未注册过！", macAddr);
        }else{
            logger.info("此设备{}进行掉线设置！", macAddr);
            device.setStatus(0);
            updateById(device);
        }
        return device;
    }

    /**
     * 根据mac地址获取设备信息
     * @param macAddr
     * @return
     */
    @Override
    public Device getDeviceByMac(String macAddr) {
        QueryWrapper<Device> qw = new QueryWrapper<>();
        qw.eq("mac_addr", macAddr);
        return getOne(qw);
    }

    /**
     * 获取设备列表
     *
     * @param search
     * @param status
     * @param buildPage
     * @return
     */
    @Override
    public ResultInfo getDeviceList(String search, Integer status,Page buildPage) {
        QueryWrapper<Device> qw = new QueryWrapper<>();
        if (StringUtil.isNotBlank(search)){
            qw.and( w -> w.like("name", search).or().like("intranet_ip", search));
        }
        if (status != null){
            qw.and(w -> w.eq("status", status));
        }
        qw.orderByDesc("create_time");
        Page page = page(buildPage, qw);
        return ResultInfo.success(page);
    }
}
