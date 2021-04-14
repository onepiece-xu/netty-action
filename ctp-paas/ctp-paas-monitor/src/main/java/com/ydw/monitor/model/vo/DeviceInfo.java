package com.ydw.monitor.model.vo;

import com.ydw.monitor.model.constant.Constants;

import java.io.Serializable;

/**
 * 设备操作参数
 * @author xulh
 * @description: TODO
 * @date 2020/11/515:13
 */
public class DeviceInfo extends DeviceStatus{

    /**
     * 设备的mac地址
     */
    protected String macAddr;
    /**
     * 设备名称
     */
    protected String deviceName;
    /**
     * 内网ip
     */
    protected String intranetIp;
    /**
     * 外网ip
     */
    protected String internetIP;
    /**
     * 操作系统版本
     */
    protected String os;
    /**
     * cpu型号规格
     */
    protected String cpuModel;
    /**
     * cpu核数
     */
    protected int cpuNumber;
    /**
     * 内存型号规格
     */
    protected String memoryModel;
    /**
     * 内存大小
     */
    protected int memorySize;
    /**
     * 显卡型号
     */
    protected String graphics;
    /**
     * 磁盘型号
     */
    protected String diskModel;
    /**
     * 磁盘大小
     */
    protected int diskSize;
    /**
     * 网卡信息
     */
    protected String networkCard;
    /**
     * 设备类型
     */
    protected int deviceType = Constants.DEVICE_ARCH_X86;


    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIntranetIp() {
        return intranetIp;
    }

    public void setIntranetIp(String intranetIp) {
        this.intranetIp = intranetIp;
    }

    public String getInternetIP() {
        return internetIP;
    }

    public void setInternetIP(String internetIP) {
        this.internetIP = internetIP;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public void setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
    }

    public int getCpuNumber() {
        return cpuNumber;
    }

    public void setCpuNumber(int cpuNumber) {
        this.cpuNumber = cpuNumber;
    }

    public int getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(int memorySize) {
        this.memorySize = memorySize;
    }

    public void setDiskSize(int diskSize) {
        this.diskSize = diskSize;
    }

    public String getMemoryModel() {
        return memoryModel;
    }

    public void setMemoryModel(String memoryModel) {
        this.memoryModel = memoryModel;
    }

    public String getGraphics() {
        return graphics;
    }

    public void setGraphics(String graphics) {
        this.graphics = graphics;
    }

    public String getDiskModel() {
        return diskModel;
    }

    public void setDiskModel(String diskModel) {
        this.diskModel = diskModel;
    }

    public String getNetworkCard() {
        return networkCard;
    }

    public void setNetworkCard(String networkCard) {
        this.networkCard = networkCard;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "macAddr='" + macAddr + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", intranetIp='" + intranetIp + '\'' +
                ", internetIP='" + internetIP + '\'' +
                ", os='" + os + '\'' +
                ", cpuModel='" + cpuModel + '\'' +
                ", cpuNumber='" + cpuNumber + '\'' +
                ", memoryModel='" + memoryModel + '\'' +
                ", memorySize='" + memorySize + '\'' +
                ", graphics='" + graphics + '\'' +
                ", diskModel='" + diskModel + '\'' +
                ", diskSize='" + diskSize + '\'' +
                ", networkCard='" + networkCard + '\'' +
                ", deviceType=" + deviceType +
                '}';
    }
}
