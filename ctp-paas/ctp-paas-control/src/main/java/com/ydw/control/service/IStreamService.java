package com.ydw.control.service;

import com.ydw.control.model.db.App;
import com.ydw.control.model.db.Device;
import com.ydw.control.model.db.Meterage;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.model.vo.StreamInfo;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:02
 */
public interface IStreamService {

    /**
     * 开流服务
     * @param device
     * @param app
     * @param meterage
     * @param client
     * @return
     */
    ResultInfo startStream(Device device, App app, Meterage meterage, int client);

    /**
     * 关闭流服务
     * @param deviceId
     * @return
     */
    ResultInfo stopStream(String deviceId);

    /**
     * 连接流服务
     * @param deviceId
     * @return
     */
    ResultInfo connectStream(String deviceId);
}
