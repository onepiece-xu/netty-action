package com.ydw.control.model.db;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author xulh
 * @since 2021-04-14
 */
public class Device implements Serializable {

    /**
     * 主键，自动生成的不重复ID
     */
    private String id;

    /**
     * 资源名称
     */
    private String name;

    /**
     * mac地址
     */
    private String macAddr;

    /**
     * 内网地址
     */
    private String intranetIp;

    /**
     * 外网地址
     */
    private String internetIp;

    /**
     * 操作系统信息
     */
    private String os;

    /**
     * cpu的型号
     */
    private String cpuModel;

    /**
     * cpu核数
     */
    private Integer cpuNumber;

    /**
     * 内存型号
     */
    private String memoryModel;

    /**
     * 内存大小
     */
    private Integer memorySize;

    /**
     * 显卡型号
     */
    private String graphics;

    /**
     * 磁盘类型
     */
    private String diskModel;

    /**
     * 磁盘大小
     */
    private Integer diskSize;

    /**
     * 网卡信息
     */
    private String networkCard;

    /**
     * 设备类型0：arm，1：pc
     */
    private int type;

    /**
     * 状态0：离线，1：在线
     */
    private int status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getInternetIp() {
        return internetIp;
    }

    public void setInternetIp(String internetIp) {
        this.internetIp = internetIp;
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

    public Integer getCpuNumber() {
        return cpuNumber;
    }

    public void setCpuNumber(Integer cpuNumber) {
        this.cpuNumber = cpuNumber;
    }

    public String getMemoryModel() {
        return memoryModel;
    }

    public void setMemoryModel(String memoryModel) {
        this.memoryModel = memoryModel;
    }

    public Integer getMemorySize() {
        return memorySize;
    }

    public void setMemorySize(Integer memorySize) {
        this.memorySize = memorySize;
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

    public Integer getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(Integer diskSize) {
        this.diskSize = diskSize;
    }

    public String getNetworkCard() {
        return networkCard;
    }

    public void setNetworkCard(String networkCard) {
        this.networkCard = networkCard;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", macAddr='" + macAddr + '\'' +
                ", intranetIp='" + intranetIp + '\'' +
                ", internetIp='" + internetIp + '\'' +
                ", os='" + os + '\'' +
                ", cpuModel='" + cpuModel + '\'' +
                ", cpuNumber=" + cpuNumber +
                ", memoryModel='" + memoryModel + '\'' +
                ", memorySize=" + memorySize +
                ", graphics='" + graphics + '\'' +
                ", diskModel='" + diskModel + '\'' +
                ", diskSize=" + diskSize +
                ", networkCard='" + networkCard + '\'' +
                ", type=" + type +
                ", status=" + status +
                '}';
    }
}
