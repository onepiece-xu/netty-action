package com.ydw.control.model.vo;

import java.io.Serializable;

/**
 * @author xulh
 * @description: TODO
 * @date 2020/11/511:03
 */
public class AppInfo extends DeviceInfo implements Serializable {

    /**
     * app的id
     */
    private String appId;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 包文件名
     */
    private String packageFileName;

    /**
     * 包文件路径
     */
    private String packageFilePath;

    /**
     * 开游戏脚本
     */
    private String startShell;

    /**
     * 关游戏脚本
     */
    private String closeShell;

    /**
     * 用户数据保存路径
     */
    private String savePath;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 游戏类型
     */
    private int type;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public String getStartShell() {
        return startShell;
    }

    public void setStartShell(String startShell) {
        this.startShell = startShell;
    }

    public String getCloseShell() {
        return closeShell;
    }

    public void setCloseShell(String closeShell) {
        this.closeShell = closeShell;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
