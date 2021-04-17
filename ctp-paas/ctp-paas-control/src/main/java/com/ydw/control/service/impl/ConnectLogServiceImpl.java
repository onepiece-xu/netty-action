package com.ydw.control.service.impl;

import com.ydw.control.model.db.ConnectLog;
import com.ydw.control.dao.ConnectLogMapper;
import com.ydw.control.service.IConnectLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.control.utils.SequenceGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xulh
 * @since 2021-04-14
 */
@Service
public class ConnectLogServiceImpl extends ServiceImpl<ConnectLogMapper, ConnectLog> implements IConnectLogService {

    @Override
    public void addLog(String deviceId, String meterageId, Integer agentStatus, Integer appStatus, Integer streamStatus) {
        ConnectLog connectLog = new ConnectLog();
        connectLog.setDeviceId(deviceId);
        connectLog.setId(SequenceGenerator.sequence());
        connectLog.setAgentStatus(agentStatus);
        connectLog.setAppStatus(appStatus);
        connectLog.setCreateTime(LocalDateTime.now());
        connectLog.setMeterageId(meterageId);
        connectLog.setStreamStatus(streamStatus);
        save(connectLog);
    }
}
