package com.ydw.monitor.service.impl;

import com.ydw.monitor.model.vo.AppInfo;
import com.ydw.monitor.model.vo.ResultInfo;
import com.ydw.monitor.service.IAppService;
import org.springframework.stereotype.Service;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:10
 */
@Service
public class ArmAppServiceImpl implements IAppService {
    /**
     * 打开app
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo startApp(AppInfo param) {
        return null;
    }

    /**
     * 关闭app
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo stopApp(AppInfo param) {
        return null;
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
