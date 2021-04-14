package com.ydw.control.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.control.model.db.App;
import com.ydw.control.model.vo.AppInfo;
import com.ydw.control.model.vo.ResultInfo;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:03
 */
public interface IAppService extends IService<App> {

    /**
     * 打开app
     * @param appId
     * @return
     */
    ResultInfo startApp(String appId);

    /**
     * 关闭app
     * @param appId
     * @return
     */
    ResultInfo stopApp(String appId);

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
