package com.ydw.monitor.service;

import com.ydw.monitor.model.vo.AppInfo;
import com.ydw.monitor.model.vo.ResultInfo;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:03
 */
public interface IAppService {

    /**
     * 打开app
     * @param param
     * @return
     */
    ResultInfo startApp(AppInfo param);

    /**
     * 关闭app
     * @param param
     * @return
     */
    ResultInfo stopApp(AppInfo param);

    /**
     * 安装app
     * @param param
     * @return
     */
    ResultInfo install(AppInfo param);

    /**
     * 卸载app
     * @param param
     * @return
     */
    ResultInfo unInstall(AppInfo param);
}
