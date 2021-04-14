package com.ydw.control.service.impl;

import com.ydw.control.model.constant.Constants;
import com.ydw.control.model.vo.DeviceInfo;
import com.ydw.control.model.vo.DeviceStatus;
import com.ydw.control.server.MessageContent;
import com.ydw.control.service.IClientService;
import com.ydw.control.utils.StringUtil;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1313:25
 */
@Service
public class ClientServiceImpl implements IClientService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    @Override
    public void init(Channel channel, MessageContent messageContent) {
        logger.info("channel-{}收到初始化指令{}！",channel.id(), messageContent.getContent());
        DeviceInfo deviceInfo = getDeviceInfo(messageContent);
        deviceInfo.setInternetIP(channel.remoteAddress().toString());
        addDeviceInfo(channel, deviceInfo);
        addClient(channel);
    }

    /**
     * 转化设备的信息
     * @param messageContent
     * @return
     */
    private DeviceInfo getDeviceInfo(MessageContent messageContent){
        DeviceInfo deviceInfo = new DeviceInfo();
        Class<? extends DeviceInfo> aClass = deviceInfo.getClass();
        Field[] fields = aClass.getDeclaredFields();
        String content = messageContent.getContent();
        String[] split = content.split("\\|");
        buildField(deviceInfo, fields, split);
        return deviceInfo;
    }

    @Override
    public void statusCheck(Channel channel, MessageContent messageContent) {
        String macAddr = getClientMacAddr(channel);
        logger.info("channel-{}，macAddr-{}，收到心跳指令{}！",channel.id(), macAddr, messageContent.getContent());
        DeviceInfo deviceInfo = getDeviceInfo(channel);
        if (deviceInfo == null){
            logger.info("为获取到客户端的设备详情！连接掐断！");
            channel.close();
        }
        int oldAgentStatus = deviceInfo.getAgentStatus();
        int oldStreamStatus = deviceInfo.getStreamStatus();
        int oldAppStatus = deviceInfo.getAppStatus();
        logger.info("设备macAddr-{}之前的状态，agentStatus:{},streamStatus:{},appStatuts:{}",
                macAddr, oldAgentStatus, oldStreamStatus, oldAppStatus);
        DeviceStatus deviceStatus = getDeviceStatus(messageContent);
        int newAgentStatus = deviceStatus.getAgentStatus();
        int newStreamStatus = deviceStatus.getStreamStatus();
        int newAppStatus = deviceStatus.getAppStatus();
        logger.info("上报的设备macAddr-{}目前的状态，agentStatus:{},streamStatus:{},appStatuts:{}",
                macAddr, newAgentStatus, newStreamStatus, newAppStatus);
        boolean changed = oldAgentStatus != newAgentStatus || oldStreamStatus != newStreamStatus || newAppStatus != newAppStatus;
        if (changed){
            logger.info("设备macAddr-{}的状态已有变化{}, 改变内存状态！", macAddr, deviceStatus);
            //状态发生改变，修改内存状态
            deviceInfo.setAgentStatus(newAgentStatus);
            deviceInfo.setStreamStatus(newStreamStatus);
            deviceInfo.setAppStatus(newAppStatus);
            addDeviceInfo(channel, deviceInfo);
            //上报客户端设备状态
            logger.info("设备macAddr-{}的状态已有变化{},开始向上报告！", macAddr, deviceStatus);
            //TODO
        }
    }

    /**
     * 转化设备状态信息
     * @param messageContent
     * @return
     */
    private DeviceStatus getDeviceStatus(MessageContent messageContent){
        DeviceStatus deviceStatus = new DeviceStatus();
        Class<? extends DeviceStatus> aClass = deviceStatus.getClass();
        Field[] fields = aClass.getDeclaredFields();
        String content = messageContent.getContent();
        String[] split = content.split("\\|");
        buildField(deviceStatus, fields, split);
        return deviceStatus;
    }

    private void buildField(Object obj, Field[] fields, String[] fieldStr){
        for (int i = 0 ; i < fieldStr.length ; i++){
            String param = fieldStr[i];
            if (StringUtil.isNotBlank(param)){
                String[] params = param.split(":");
                String key = params[0];
                String value = params[1];
                for (Field field : fields){
                    if (field.getName().equals(key)){
                        try {
                            field.setAccessible(true);
                            if(NumberUtils.isNumber(value)){
                                field.set(obj, Integer.valueOf(value));
                            }else{
                                field.set(obj, value);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void sendCommand(String macAddr, Object command) {
        Channel channel = channelMap.get(macAddr);
        if (channel == null || !channel.isActive()){
            logger.error("发送给{}指令{}时，channel未处于active状态！", macAddr,command.toString());
            throw new RuntimeException("发送给指令时，channel未处于active状态！");
        }
        logger.info("发送给{}指令{}！", macAddr,command.toString());
        sendCommand(channel, command);
    }

    @Override
    public void sendCommand(Channel channel, Object command) {
        channel.write(command);
    }

    @Override
    public void addClient(Channel channel) {
        DeviceInfo deviceInfo = getDeviceInfo(channel);
        if (deviceInfo == null){
            logger.error("此channel还没有进行注册！不能执行addClient方法！");
            return;
        }
        logger.info("新注册一个客户端{}，并开始往上报告！", deviceInfo);
        channelMap.put(deviceInfo.getMacAddr(), channel);
        //TODO

    }

    @Override
    public void removeClient(Channel channel) {
        String macAddr = getClientMacAddr(channel);
        if (StringUtil.isBlank(macAddr)){
            logger.error("此channel还没有进行注册！不能执行removeClient方法！");
            return;
        }
        removeClient(macAddr);
    }

    @Override
    public void removeClient(String macAddr) {
        channelMap.remove(macAddr);
        logger.info("掉线一个客户端macAddr-{}，并开始往上报告！", macAddr);
        //TODO
    }

    @Override
    public String getClientMacAddr(Channel channel) {
        String macAddr = null;
        DeviceInfo deviceInfo = getDeviceInfo(channel);
        if (deviceInfo != null){
            macAddr = deviceInfo.getMacAddr();
        }else{
            logger.error("此channel还没有进行注册！不能执行getClientMacAddr方法！");
        }
        return macAddr;
    }

    @Override
    public DeviceInfo getDeviceInfo(Channel channel){
        AttributeKey<DeviceInfo> deviceInfoKey = AttributeKey.valueOf(Constants.CHANNEL_DEVICE_KEY);
        return channel.attr(deviceInfoKey).get();
    }

    @Override
    public void addDeviceInfo(Channel channel, DeviceInfo deviceInfo) {
        AttributeKey<DeviceInfo> deviceInfoKey = AttributeKey.valueOf(Constants.CHANNEL_DEVICE_KEY);
        channel.attr(deviceInfoKey).set(deviceInfo);
    }
}