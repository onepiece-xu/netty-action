package com.ydw.control.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.control.dao.AppMapper;
import com.ydw.control.dao.DeviceMapper;
import com.ydw.control.model.db.App;
import com.ydw.control.model.db.Device;
import com.ydw.control.model.vo.AppInfo;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.service.IAppService;
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

    /**
     * 打开app
     * @param appId
     * @return
     */
    @Override
    public ResultInfo startApp(String appId) {
        if (StringUtil.isBlank(appId)){
            appId = "";
        }
        App app = super.getById(appId);
        return monitorService.startApp(app);
    }

    /**
     * 关闭app
     *
     * @param appId
     * @return
     */
    @Override
    public ResultInfo stopApp(String appId) {
        if (StringUtil.isBlank(appId)){
            appId = "";
        }
        App app = super.getById(appId);
        return monitorService.stopApp(app);
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
