package com.ydw.monitor.service.impl;

import com.ydw.monitor.model.constant.Constants;
import com.ydw.monitor.model.vo.AppInfo;
import com.ydw.monitor.model.vo.ResultInfo;
import com.ydw.monitor.server.FullMessage;
import com.ydw.monitor.server.MessageContent;
import com.ydw.monitor.server.MessageHeader;
import com.ydw.monitor.service.IAppService;
import com.ydw.monitor.service.IClientService;
import com.ydw.monitor.utils.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1217:10
 */
@Service
public class PcAppServiceImpl implements IAppService {

    @Autowired
    private IClientService clientService;

    /**
     * 打开app
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo startApp(AppInfo param) {
        FullMessage message = buildMessage(param, Constants.COMMAND_APP_START);
        boolean b = clientService.sendCommand(param.getMacAddr(), message);
        return b ? ResultInfo.success() : ResultInfo.fail();
    }

    /**
     * 关闭app
     *
     * @param param
     * @return
     */
    @Override
    public ResultInfo stopApp(AppInfo param) {
        FullMessage message = buildMessage(param, Constants.COMMAND_APP_STOP);
        boolean b = clientService.sendCommand(param.getMacAddr(), message);
        return b ? ResultInfo.success() : ResultInfo.fail();
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

    /**
     * 构建消息
     * @param param
     * @param command
     * @return
     */
    private FullMessage buildMessage(AppInfo param, int command){
        //简单消息头
        MessageHeader messageHeader = MessageHeader.SimpleMessageHeader(command);
        //构建消息内容
        StringBuffer commandParam = new StringBuffer();
        //    app的id
        if (StringUtil.isNotBlank(param.getAppId())) {
            commandParam.append("|").append(Constants.APPID).append(param.getAppId());
        }
        //    用户id
        if (StringUtil.isNotBlank(param.getUserId())) {
            commandParam.append("|").append(Constants.USERID).append(param.getUserId());
        }
        //    存档地址savePath
        if (StringUtil.isNotBlank(param.getSavePath())) {
            commandParam.append("|").append(Constants.SAVEPATH).append(param.getSavePath());
        }
        //    app的文件名
        if (StringUtil.isNotBlank(param.getPackageFileName())) {
            commandParam.append("|").append(Constants.APP_PARAMETER).append(param.getPackageFileName());
        }
        //    app路径
        if (StringUtil.isNotBlank(param.getPackageFilePath())) {
            commandParam.append("|").append(Constants.PATH_PARAMETER).append(param.getPackageFilePath());
        }
        //    app的开启shell脚本
        if(StringUtil.isNotBlank(param.getStartShell())){
            commandParam.append("|").append(Constants.LUNCHER).append(param.getStartShell());
        }
        MessageContent messageContent = new MessageContent(commandParam);
        return new FullMessage(messageHeader, messageContent);
    }
}
