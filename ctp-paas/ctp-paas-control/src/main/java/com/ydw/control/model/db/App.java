package com.ydw.control.model.db;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xulh
 * @since 2021-04-14
 */
public class App implements Serializable {

    private static final long serialVersionUID=1L;

    /**
     * 自动生成ID
     */
    private String id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用描述
     */
    private String description;

    /**
     * 安装包名称
     */
    private String packageName;

    /**
     * 安装包文件名称
     */
    private String packageFileName;

    /**
     * 安装包路径
     */
    private String packageFilePath;

    /**
     * 应用大小
     */
    private Integer size;

    /**
     * 安装后的实际Realsize大小
     */
    private Integer realSize;

    /**
     * 应用类型：arm/x86     0: arm  1: x86   2：云游戏   3：云电脑，4：云手机
     */
    private Integer type;

    /**
     * 账号信息存放的路径

     */
    private String accountPath;

    /**
     * 用户数据存放路径
     */
    private String savePath;

    /**
     * 开始前执行脚本
     */
    private String openShell;

    /**
     * 关闭后执行脚本
     */
    private String closeShell;

    /**
     * arm端使用的策略编号
     */
    private Integer armStrategyId;

    /**
     * pc端使用的流策略Id
     */
    private Integer pcStrategyId;

    /**
     * 0 预留  1横屏 2 竖屏
     */
    private Integer screen;


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageFileName() {
        return packageFileName;
    }

    public void setPackageFileName(String packageFileName) {
        this.packageFileName = packageFileName;
    }

    public String getPackageFilePath() {
        return packageFilePath;
    }

    public void setPackageFilePath(String packageFilePath) {
        this.packageFilePath = packageFilePath;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getRealSize() {
        return realSize;
    }

    public void setRealSize(Integer realSize) {
        this.realSize = realSize;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAccountPath() {
        return accountPath;
    }

    public void setAccountPath(String accountPath) {
        this.accountPath = accountPath;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getOpenShell() {
        return openShell;
    }

    public void setOpenShell(String openShell) {
        this.openShell = openShell;
    }

    public String getCloseShell() {
        return closeShell;
    }

    public void setCloseShell(String closeShell) {
        this.closeShell = closeShell;
    }

    public Integer getArmStrategyId() {
        return armStrategyId;
    }

    public void setArmStrategyId(Integer armStrategyId) {
        this.armStrategyId = armStrategyId;
    }

    public Integer getPcStrategyId() {
        return pcStrategyId;
    }

    public void setPcStrategyId(Integer pcStrategyId) {
        this.pcStrategyId = pcStrategyId;
    }

    public Integer getScreen() {
        return screen;
    }

    public void setScreen(Integer screen) {
        this.screen = screen;
    }

    @Override
    public String toString() {
        return "App{" +
        "id=" + id +
        ", name=" + name +
        ", description=" + description +
        ", packageName=" + packageName +
        ", packageFileName=" + packageFileName +
        ", packageFilePath=" + packageFilePath +
        ", size=" + size +
        ", realSize=" + realSize +
        ", type=" + type +
        ", accountPath=" + accountPath +
        ", savePath=" + savePath +
        ", openShell=" + openShell +
        ", closeShell=" + closeShell +
        ", armStrategyId=" + armStrategyId +
        ", pcStrategyId=" + pcStrategyId +
        ", screen=" + screen +
        "}";
    }
}
