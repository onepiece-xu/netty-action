package com.ydw.control.service.impl;

import com.alibaba.fastjson.JSON;
import com.ydw.control.model.constant.Constants;
import com.ydw.control.model.db.AppStrategy;
import com.ydw.control.model.emuns.AppTypeEnum;
import com.ydw.control.model.vo.ResultInfo;
import com.ydw.control.model.vo.StreamInfo;
import com.ydw.control.model.vo.WebrtcConfig;
import com.ydw.control.service.IStreamService;
import com.ydw.control.utils.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:11
 */
@Service
public class StreamServiceImpl implements IStreamService {

    Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 开流服务
     * @param param
     * @return
     */
    @Override
    public ResultInfo startStream(StreamInfo param) {
        logger.info("连接{},设备{},app{},客户端{},连接类型{}准备开启流服务", connectId,deviceInfo.getDeviceId(),appInfo.getId(),client,connectType);
        ResultInfo resultInfo = null;
        try {
            Integer strategyId = null;
            if (client == Constants.CLIENT_TYPE_MOBILE) {
                strategyId = appInfo.getStrategyId();
            } else {
                strategyId = appInfo.getPcStrategyId() == null ? appInfo.getStrategyId()
                        : appInfo.getPcStrategyId();
            }
            AppStrategy appStrategy = appStrategyMapper.selectById(strategyId);
            // 是否以webrtc的形式申请设备
            WebrtcConfig webRtcConfig = null;
            String url = deviceInfo.getApiUrl() + Constant.URL_EDGE_STARTSTREAM;
            HashMap<String, Object> httpParams = new HashMap<>();
            httpParams.put("deviceId", deviceInfo.getDeviceId());
            httpParams.put("deviceType", deviceInfo.getDeviceType());
            httpParams.put("innerIp", deviceInfo.getInnerIp());
            httpParams.put("innerPort", deviceInfo.getInnerPort());
            httpParams.put("adbIp", deviceInfo.getAdbIp());
            httpParams.put("adbPort", deviceInfo.getAdbPort());
            httpParams.put("model", deviceInfo.getModel());

            httpParams.put("type", appInfo.getType() == 0 || appInfo.getType() == 1 ? AppTypeEnum.GAME.type : appInfo.getType());
            httpParams.put("screen", appInfo.getScreen());
            httpParams.put("fps", appStrategy.getFps());
            httpParams.put("codec", Constant.DEVICE_ENCODE_H265.equals(appStrategy.getEncode()) ? 1 : 0);
            httpParams.put("video", appStrategy.getVideo());
            httpParams.put("speed", appStrategy.getSpeed());
            httpParams.put("connectId", connectId);
            httpParams.put("token", getTicket(connectId,account));
            httpParams.put("webRTC", true);
            httpParams.put("signalServer", webRtcConfig.getSignalServer());
            httpParams.put("turnServer", String.join(",", webRtcConfig.getStunUrl(),
                    webRtcConfig.getTurnTcpUrl(), webRtcConfig.getTurnUdpUrl()));
            httpParams.put("turnUser", webRtcConfig.getTurnUser());
            httpParams.put("turnPassword", webRtcConfig.getTurnPassword());
            Map<String,String> headers = ydwClusterService.buildHeader(deviceInfo.getClusterId());
            logger.info("url[{}]",url);
            logger.info("headers[{}]", JSON.toJSONString(headers));
            logger.info("httpParams[{}]",JSON.toJSONString(httpParams));
            String result = HttpUtil.doJsonPost(url,headers, httpParams);
            logger.info("启动此连接{}的游戏流服务返回结果：{}", connectId,result);
            resultInfo = JSON.parseObject(result, ResultInfo.class);
            if(resultInfo.getCode() == 200){
                resultInfo.setData(webRtcConfig);
            }else{
                throw new RuntimeException();
            }
        } catch (Exception e) {
            logger.error("连接{},设备{},app{},客户端{},连接类型{}开启流服务失败！",connectId,deviceInfo.getDeviceId(),appInfo.getId(),client,connectType);
            logger.error(e.getMessage());
            resultInfo = ResultInfo.fail();
        }
        return resultInfo;
    }

    /**
     * 关闭流服务
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo stopStream(StreamInfo param) {
        return null;
    }

    /**
     * 连接流服务
     * @param deviceId
     * @return
     */
    @Override
    public ResultInfo connectStream(String deviceId) {
        return null;
    }
}
