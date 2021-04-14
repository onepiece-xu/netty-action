package com.ydw.control.model.db;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class AppStrategy implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自增ID 
     */
    private Integer id;

    /**
     * 策略名称
     */
    private String name;

    /**
     * 启动帧率
     */
    private Integer fps;

    /**
     * 启动码率
     */
    private Integer speed;

    /**
     * 画质设置
     */
    private Integer video;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 启动输出分辨率
     */
    private Integer output;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;

    /**
     * 编码方式
     */
    private String encode;

    /**
     * 0 默认的流策略，不可被删                                               1  可被删除的流策略
     */
    private Integer type;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFps() {
        return fps;
    }

    public void setFps(Integer fps) {
        this.fps = fps;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public Integer getVideo() {
        return video;
    }

    public void setVideo(Integer video) {
        this.video = video;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getOutput() {
        return output;
    }

    public void setOutput(Integer output) {
        this.output = output;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AppStrategy{" +
        "id=" + id +
        ", name=" + name +
        ", fps=" + fps +
        ", speed=" + speed +
        ", video=" + video +
        ", createTime=" + createTime +
        ", output=" + output +
        ", updateTime=" + updateTime +
        ", encode=" + encode +
        ", type=" + type +
        "}";
    }
}
