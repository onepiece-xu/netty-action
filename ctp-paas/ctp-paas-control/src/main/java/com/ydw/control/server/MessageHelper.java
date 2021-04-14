package com.ydw.control.server;

import com.ydw.control.model.constant.Constants;
import com.ydw.control.service.IClientService;
import com.ydw.control.utils.ThreadPool;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1314:56
 */
@Component
public class MessageHelper {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ThreadPool threadPool;

    @Autowired
    private IClientService clientService;

    public MessageHeader getMessageHeader( ByteBuf byteBuf){
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setCmdType(byteBuf.readShort());
        messageHeader.setCsType(byteBuf.readShort());
        messageHeader.setCommandId(byteBuf.readInt());
        messageHeader.setPacketId(byteBuf.readInt());
        messageHeader.setTimeStamp(byteBuf.readInt());
        messageHeader.setContentLength(byteBuf.readInt());
        return messageHeader;
    }

    public MessageContent getMessageContent( ByteBuf byteBuf){
        MessageContent messageContent = new MessageContent();
        CharSequence charSequence = byteBuf.readCharSequence(byteBuf.readableBytes(), Charset.forName("UTF-8"));
        messageContent.setContent(charSequence);
        return messageContent;
    }

    public void dispatchMessage(Channel channel, ByteBuf byteBuf){
        MessageHeader messageHeader = getMessageHeader(byteBuf);
        MessageContent messageContent = getMessageContent(byteBuf);
        dispatchMessage(channel, messageHeader, messageContent);
    }

    public void dispatchMessage(Channel channel, MessageHeader messageHeader, MessageContent messageContent){
        int commandId = messageHeader.getCommandId();
        logger.info("channel-{}收到commandId为{}，msg为{}", channel.id(),commandId, messageContent.getContent());
        switch (commandId){
            case Constants.COMMAND_SYSTEM_INIT:
                logger.info("channel-{}指令为初始化指令，开始初始化！", channel.id(), commandId, messageContent.getContent());
                threadPool.execute(() -> clientService.init(channel, messageContent));
                break;
            case Constants.COMMAND_SYSTEM_STATUS:
                logger.info("channel-{}指令为状态上报指令，开始做状态检查！", channel.id(), commandId, messageContent.getContent());
                threadPool.execute(() -> clientService.statusCheck(channel, messageContent));
                break;
        }
    }
}
