package com.ydw.control.service.impl;

import com.alibaba.fastjson.JSON;
import com.ydw.control.model.constant.Constants;
import com.ydw.control.model.db.*;
import com.ydw.control.model.emuns.AppTypeEnum;
import com.ydw.control.model.vo.ConnectStreamVO;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.model.vo.StreamInfo;
import com.ydw.control.model.vo.WebrtcConfig;
import com.ydw.control.service.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:11
 */
@Service
public class StreamServiceImpl implements IStreamService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private IMonitorService monitorService;

    @Autowired
    private IDeviceService deviceService;

    @Autowired
    private IAppService appService;

    @Autowired
    private IAppStrategyService appStrategyService;

    @Autowired
    private ISignalServerService signalServerService;

    @Autowired
    private ITurnServerService turnServerService;

    @Autowired
    private IMeterageService meterageService;

    /**
     * 开流服务
     * @param device
     * @param app
     * @param meterage
     * @param client
     * @return
     */
    @Override
    public ResultInfo startStream(Device device, App app, Meterage meterage, int client) {
        StreamInfo streamInfo = new StreamInfo();
        streamInfo.setMacAddr(device.getMacAddr());
        streamInfo.setDeviceType(device.getType());
        streamInfo.setType(app.getType());

        Integer strategyId = null;
        if (client == Constants.CLIENT_TYPE_MOBILE) {
            strategyId = app.getArmStrategyId();
        } else {
            strategyId = app.getPcStrategyId() == null ? app.getArmStrategyId()
                    : app.getPcStrategyId();
        }
        streamInfo.setScreen(app.getScreen());
        AppStrategy appStrategy = appStrategyService.getById(strategyId);
        streamInfo.setFps(appStrategy.getFps());
        streamInfo.setCodec(Constants.DEVICE_ENCODE_H265.equals(appStrategy.getEncode()) ? 1 : 0);
        streamInfo.setVideo(appStrategy.getVideo());
        streamInfo.setSpeed(appStrategy.getSpeed());

        streamInfo.setConnectId(device.getId());
        streamInfo.setToken(DigestUtils.md5Hex(device.getId()));

        SignalServer signalServer = signalServerService.list().get(0);
        TurnServer turnServer = turnServerService.list().get(0);
        WebrtcConfig webRtcConfig = new WebrtcConfig(signalServer,turnServer);
        streamInfo.setWebRTC(true);
        streamInfo.setSignalServer(webRtcConfig.getSignalServer());
        streamInfo.setTurnServer(String.join(",", webRtcConfig.getStunUrl(),
                webRtcConfig.getTurnTcpUrl(), webRtcConfig.getTurnUdpUrl()));
        streamInfo.setTurnUser(webRtcConfig.getTurnUser());
        streamInfo.setTurnPassword(webRtcConfig.getTurnPassword());
        logger.info("开始开启流服务！{}", streamInfo);
        ResultInfo resultInfo = monitorService.startStream(streamInfo);
        logger.info("启动此设备{}的游戏流服务返回结果：{}", device.getId(), JSON.toJSONString(resultInfo));
        ConnectStreamVO scv = new ConnectStreamVO(streamInfo.getToken(),streamInfo.getConnectId(),device.getName(),webRtcConfig);
        resultInfo.setData(scv);
        return resultInfo;
    }

    /**
     * 关闭流服务
     *
     * @param deviceId
     * @return
     */
    @Override
    public ResultInfo stopStream(String deviceId) {
        Device device = deviceService.getById(deviceId);
        StreamInfo streamInfo = new StreamInfo();
        streamInfo.setMacAddr(device.getMacAddr());
        streamInfo.setDeviceType(device.getType());
        return monitorService.stopStream(streamInfo);
    }

    /**
     * 连接流服务
     * @param deviceId
     * @return
     */
    @Override
    public ResultInfo connectStream(String deviceId) {
        Device device = deviceService.getById(deviceId);
        App app = appService.list().get(0);
        Meterage meterage = meterageService.getUnCompleteMeterageById(deviceId);
        int client = Constants.CLIENT_TYPE_PC;
        return startStream(device, app, meterage, client);
    }
}
