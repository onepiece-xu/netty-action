package com.ydw.monitor.service;

import com.ydw.monitor.model.vo.DeviceInfo;
import com.ydw.monitor.server.FullMessage;
import com.ydw.monitor.server.MessageContent;
import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1313:22
 */
public interface IClientService {

    /**
     * 客户端注册
     * @param channel
     * @param messageContent
     */
    void init(Channel channel, MessageContent messageContent);

    /**
     * 客户端状态检测
     * @param channel
     * @param messageContent
     */
    void statusCheck(Channel channel, MessageContent messageContent);

    /**
     * 往客户端发送指令
     * @param macAddr
     * @param fullMessage
     */
    boolean sendCommand(String macAddr, FullMessage fullMessage);

    /**
     * 往客户端发送指令
     * @param channel
     * @param fullMessage
     */
    boolean sendCommand(Channel channel, FullMessage fullMessage);

    /**
     * 添加客户端
     * @param channel
     */
    void addClient(Channel channel);

    /**
     * 客户端断开移除
     * @param channel
     */
    void removeClient(Channel channel);

    /**
     * 客户端断开移除
     * @param macAddr
     */
    void removeClient(String macAddr);

    /**
     * 获取客户端的mac地址
     * @param channel
     * @return
     */
    String getClientMacAddr(Channel channel);

    /**
     * 获取客户端的设备信息
     * @param channel
     * @return
     */
    DeviceInfo getDeviceInfo(Channel channel);

    /**
     * 获取客户端的设备信息
     * @param macAddr
     * @return
     */
    DeviceInfo getDeviceInfo(String macAddr);

    /**
     * 添加客户端的设备信息
     * @param channel
     * @param deviceInfo
     */
    void addDeviceInfo(Channel channel, DeviceInfo deviceInfo);

    /**
     * 获取所有客户端
     * @return
     */
    List<DeviceInfo> getAllClient();

    /**
     * 上报此客户端的状态
     * @param macAddr
     */
    void reportClient(String macAddr);
}
