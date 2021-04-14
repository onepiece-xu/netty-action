package com.ydw.monitor.server;

import com.ydw.monitor.utils.StringUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.UnsupportedEncodingException;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1415:26
 */
public class FullMessage {

    /**
     * 消息头
     */
    private MessageHeader messageHeader;

    /**
     * 消息体
     */
    private MessageContent messageContent;

    public FullMessage() {
    }

    public FullMessage(MessageHeader messageHeader, MessageContent messageContent) {
        this.messageHeader = messageHeader;
        this.messageContent = messageContent;
    }

    public MessageHeader getMessageHeader() {
        return messageHeader;
    }

    public void setMessageHeader(MessageHeader messageHeader) {
        this.messageHeader = messageHeader;
    }

    public MessageContent getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(MessageContent messageContent) {
        this.messageContent = messageContent;
    }

    @Override
    public String toString() {
        return "FullMessage{" +
                "messageHeader=" + messageHeader +
                ", messageContent=" + messageContent +
                '}';
    }
}
