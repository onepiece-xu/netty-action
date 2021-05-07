package com.ydw.monitor.model.vo;

import java.io.Serializable;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1316:27
 */
public class DeviceStatus implements Serializable {

    /**
     * mac地址
     */
    protected String macAddr;
    /**
     * agent状态，0：未启动，1：正常启动
     */
    protected int agentStatus = 0;
    /**
     * app状态，0：未启动，1：正常启动
     */
    protected int appStatus = 0;
    /**
     * 流服务状态，0：未启动，1：正常启动
     */
    protected int streamStatus = 0;

    public DeviceStatus() {
    }

    public DeviceStatus(String macAddr, int agentStatus, int appStatus, int streamStatus) {
        this.macAddr = macAddr;
        this.agentStatus = agentStatus;
        this.appStatus = appStatus;
        this.streamStatus = streamStatus;
    }

    public int getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(int agentStatus) {
        this.agentStatus = agentStatus;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }

    public int getStreamStatus() {
        return streamStatus;
    }

    public void setStreamStatus(int streamStatus) {
        this.streamStatus = streamStatus;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    @Override
    public String toString() {
        return "DeviceStatus{" +
                "macAddr='" + macAddr + '\'' +
                ", agentStatus=" + agentStatus +
                ", appStatus=" + appStatus +
                ", streamStatus=" + streamStatus +
                '}';
    }
}
