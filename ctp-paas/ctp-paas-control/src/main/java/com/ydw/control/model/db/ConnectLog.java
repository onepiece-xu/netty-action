package com.ydw.control.model.db;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xulh
 * @since 2021-04-14
 */
public class ConnectLog implements Serializable {

    /**
     * 连接ID自动生成
     */
    private String id;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 计量id
     */
    private String meterageId;

    /**
     * app状态0：未开启，1：已开启
     */
    private Integer agentStatus;

    /**
     * app状态0：未开启，1：已开启
     */
    private Integer appStatus;

    /**
     * 流服务状态0：未开启，1：已开启
     */
    private Integer streamStatus;

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

    public String getMeterageId() {
        return meterageId;
    }

    public void setMeterageId(String meterageId) {
        this.meterageId = meterageId;
    }

    public Integer getAgentStatus() {
        return agentStatus;
    }

    public void setAgentStatus(Integer agentStatus) {
        this.agentStatus = agentStatus;
    }

    public Integer getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Integer appStatus) {
        this.appStatus = appStatus;
    }

    public Integer getStreamStatus() {
        return streamStatus;
    }

    public void setStreamStatus(Integer streamStatus) {
        this.streamStatus = streamStatus;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "ConnectLog{" +
                "id='" + id + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", meterageId='" + meterageId + '\'' +
                ", agentStatus=" + agentStatus +
                ", appStatus=" + appStatus +
                ", streamStatus=" + streamStatus +
                ", createTime=" + createTime +
                '}';
    }
}
