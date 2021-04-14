package com.ydw.monitor.service.impl;

import com.ydw.monitor.model.constant.Constants;
import com.ydw.monitor.model.vo.DeviceInfo;
import com.ydw.monitor.model.vo.ResultInfo;
import com.ydw.monitor.server.FullMessage;
import com.ydw.monitor.server.MessageHeader;
import com.ydw.monitor.service.IClientService;
import com.ydw.monitor.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:08
 */
@Service
public class PcDeviceServiceImpl implements IDeviceService {

    @Autowired
    private IClientService clientService;

    /**
     * 重启
     * @param param
     * @return
     */
    @Override
    public ResultInfo reboot(DeviceInfo param) {
        FullMessage message = buildMessage(param, Constants.COMMAND_SYSTEM_REBOOT);
        boolean b = clientService.sendCommand(param.getMacAddr(), message);
        return b ? ResultInfo.success() : ResultInfo.fail();
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

    /**
     * 构建消息
     * @param param
     * @param command
     * @return
     */
    private FullMessage buildMessage(DeviceInfo param, int command){
        return new FullMessage(MessageHeader.SimpleMessageHeader(command), null);
    }
}
