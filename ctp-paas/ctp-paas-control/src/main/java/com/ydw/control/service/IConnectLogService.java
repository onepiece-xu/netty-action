package com.ydw.control.service;

import com.ydw.control.model.db.ConnectLog;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xulh
 * @since 2021-04-14
 */
public interface IConnectLogService extends IService<ConnectLog> {

    void addLog(String deviceId, String meterageId, Integer agentStatus, Integer appStatus, Integer streamStatus);
}
