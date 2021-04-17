package com.ydw.control.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.control.dao.AppMapper;
import com.ydw.control.dao.DeviceMapper;
import com.ydw.control.model.db.App;
import com.ydw.control.model.db.Device;
import com.ydw.control.model.vo.AppInfo;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IAppService;
import com.ydw.control.service.IDeviceService;
import com.ydw.control.service.IMonitorService;
import com.ydw.control.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:10
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements IAppService {

    @Autowired
    private IMonitorService monitorService;

    @Autowired()
    private IDeviceService deviceService;

    /**
     * 打开app
     * @param appId
     * @return
     */
    @Override
    public ResultInfo startApp(String deviceId, String appId) {
        App app = super.list().get(0);
        Device device = deviceService.getById(deviceId);
        return startApp(device, app);
    }

    /**
     * 打开app
     * @param device
     * @param app
     * @return
     */
    @Override
    public ResultInfo startApp(Device device, App app) {
        AppInfo appInfo = new AppInfo();
        appInfo.setAppId(app.getId());
        appInfo.setCloseShell(app.getCloseShell());
        appInfo.setPackageFileName(app.getPackageFileName());
        appInfo.setPackageFilePath(app.getPackageFilePath());
        appInfo.setPackageName(app.getPackageName());
        appInfo.setSavePath(app.getSavePath());
        appInfo.setType(app.getType());
        appInfo.setStartShell(app.getOpenShell());
        appInfo.setCloseShell(app.getCloseShell());
        appInfo.setMacAddr(device.getMacAddr());
        return monitorService.startApp(appInfo);
    }

    /**
     * 关闭app
     *
     * @param appId
     * @return
     */
    @Override
    public ResultInfo stopApp(String deviceId, String appId) {
        App app = super.list().get(0);
        AppInfo appInfo = new AppInfo();
        appInfo.setAppId(app.getId());
        appInfo.setCloseShell(app.getCloseShell());
        appInfo.setPackageFileName(app.getPackageFileName());
        appInfo.setPackageFilePath(app.getPackageFilePath());
        appInfo.setPackageName(app.getPackageName());
        appInfo.setSavePath(app.getSavePath());
        appInfo.setType(app.getType());
        appInfo.setStartShell(app.getOpenShell());
        appInfo.setCloseShell(app.getCloseShell());
        Device device = deviceService.getById(deviceId);
        appInfo.setMacAddr(device.getMacAddr());
        return monitorService.stopApp(appInfo);
    }

    /**
     * 安装app
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo install(AppInfo param) {
        return null;
    }

    /**
     * 卸载app
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo unInstall(AppInfo param) {
        return null;
    }
}
