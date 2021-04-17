package com.ydw.control.model.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/169:26
 */
public class MeterageListVO implements Serializable {

    /**
     * 连接id
     */
    private String id;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 设备mac地址
     */
    private String macAddr;

    /**
     * 设备内网ip
     */
    private String intranetIp;

    /**
     * 计量开始时间
     */
    private LocalDateTime beginTime;

    /**
     * 计量结束时间
     */
    private LocalDateTime endTime;

    /**
     * 计量总时长（min）
     */
    private int totalTime;

    /**
     * 计量状态（0：未结束，1：已结束）
     */
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getIntranetIp() {
        return intranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        this.intranetIp = intranetIp;
    }

    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
