package com.ydw.control.service;

import com.ydw.control.model.vo.DeviceInfo;
import com.ydw.control.server.MessageContent;
import io.netty.channel.Channel;

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
     * @param command
     */
    void sendCommand(String macAddr, Object command);

    /**
     * 往客户端发送指令
     * @param channel
     * @param command
     */
    void sendCommand(Channel channel, Object command);

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
     * 添加客户端的设备信息
     * @param channel
     * @param deviceInfo
     */
    void addDeviceInfo(Channel channel, DeviceInfo deviceInfo);
}
