package com.ydw.control.model.db;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 集群（网吧）
 * </p>
 *
 * @author xulh
 * @since 2021-04-27
 */
public class Cluster implements Serializable {

    private static final long serialVersionUID=1L;

    private Integer id;

    /**
     * 名称
     */
    private String name;

    private String appId;

    /**
     * 描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Cluster{" +
        "id=" + id +
        ", name=" + name +
        ", appId=" + appId +
        ", description=" + description +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}";
    }
}
