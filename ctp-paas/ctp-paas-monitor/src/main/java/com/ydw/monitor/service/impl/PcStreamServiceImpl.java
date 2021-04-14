package com.ydw.monitor.service.impl;

import com.ydw.monitor.model.constant.Constants;
import com.ydw.monitor.model.vo.AppInfo;
import com.ydw.monitor.model.vo.ResultInfo;
import com.ydw.monitor.model.vo.ScreenTransfer;
import com.ydw.monitor.model.vo.StreamInfo;
import com.ydw.monitor.server.FullMessage;
import com.ydw.monitor.server.MessageContent;
import com.ydw.monitor.server.MessageHeader;
import com.ydw.monitor.service.IClientService;
import com.ydw.monitor.service.IStreamService;
import com.ydw.monitor.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:11
 */
@Service
public class PcStreamServiceImpl implements IStreamService {

    @Autowired
    private IClientService clientService;

    /**
     * 开流服务
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo startStream(StreamInfo param) {
        FullMessage message = buildMessage(param, Constants.COMMAND_SYSTEM_REBOOT);
        boolean b = clientService.sendCommand(param.getMacAddr(), message);
        return b ? ResultInfo.success() : ResultInfo.fail();
    }

    /**
     * 关闭流服务
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo stopStream(StreamInfo param) {
        FullMessage message = buildMessage(param, Constants.COMMAND_SYSTEM_REBOOT);
        boolean b = clientService.sendCommand(param.getMacAddr(), message);
        return b ? ResultInfo.success() : ResultInfo.fail();
    }

    /**
     * 构建消息
     * @param param
     * @param command
     * @return
     */
    private FullMessage buildMessage(StreamInfo param, int command){
        //简单消息头
        MessageHeader messageHeader = MessageHeader.SimpleMessageHeader(command);
        //构建参数
        StringBuffer commandParam = new StringBuffer();
        /// 视频参数，需要同时确定横 、竖屏
        if (param.getVideo() != null){
            commandParam.append("|").append(Constants.VIDEO_PARAMETER).append(param.getVideo());
            ScreenTransfer sc = new ScreenTransfer(param.getVideo(), param.getScreen());
            commandParam.append("|").append(Constants.VIDEO_WIDTH_PARAMETER).append(sc.getWidth());
            commandParam.append("|").append(Constants.VIDEO_HEIGTH_PARAMETER).append(sc.getHeigth());
        }
        /// 视频参数，码率
        if (param.getSpeed() != null) {
            commandParam.append("|").append(Constants.BITRATE_PARAMETER).append(param.getSpeed());
        }
        //  视频参数，帧率
        if (param.getFps() != null) {
            commandParam.append("|").append(Constants.FPS_PARAMETER).append(param.getFps());
        }
        //   编码格式
        if (param.getCodec() != null) {
            commandParam.append("|").append(Constants.CODEC_PARAMETER ).append(param.getCodec());
        }
        //   连接凭证
        if (param.getToken() != null) {
            commandParam.append("|").append(Constants.TOKEN_PARAMETER).append(param.getToken());
        }
        //   连接id
        if (param.getConnectId() != null) {
            commandParam.append("|").append(Constants.CONNECTID_PARAMETER).append(param.getConnectId());
        }
        //    webRtc相关参数
        if (param.getWebRTC()) {
            commandParam.append("|").append(Constants.WEBRTC_PARAMETER).append(1);
            if (param.getSignalServer() != null) {
                commandParam.append("|").append(Constants.SIGNALSERVER_PARAMETER).append(param.getSignalServer());
            }
            if (param.getTurnServer() != null) {
                commandParam.append("|").append(Constants.TURNSERVER_PARAMETER).append(param.getTurnServer());
            }
            if (param.getTurnUser() != null) {
                commandParam.append("|").append(Constants.TURNUSER_PARAMETER).append(param.getTurnUser());
            }
            if (param.getTurnPassword() != null) {
                commandParam.append("|").append(Constants.TURNPASSWORD_PARAMETER).append(param.getTurnPassword());
            }
        }
        MessageContent messageContent = new MessageContent(commandParam);
        return new FullMessage(messageHeader,messageContent);
    }
}
