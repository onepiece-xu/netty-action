package com.ydw.monitor.server;

import io.netty.buffer.ByteBuf;

import java.time.LocalDateTime;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1314:52
 */
public class MessageHeader {

    //指令类型
    private short cmdType;
    //Client/Server标识
    // c-s 0X01; s-c 0X02
    private short csType;
    //指令id
    private int commandId;
    //数据包id，用随机数即可
    private int packetId;
    //时间戳，只精确到秒
    private int timeStamp;
    //内容大小
    private int contentLength;

    public short getCmdType() {
        return cmdType;
    }

    public void setCmdType(short cmdType) {
        this.cmdType = cmdType;
    }

    public short getCsType() {
        return csType;
    }

    public void setCsType(short csType) {
        this.csType = csType;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public int getPacketId() {
        return packetId;
    }

    public void setPacketId(int packetId) {
        this.packetId = packetId;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public static MessageHeader SimpleMessageHeader(int commandId){
        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setCmdType((short)2);
        messageHeader.setCsType((short)2);
        messageHeader.setCommandId(commandId);
        messageHeader.setTimeStamp(LocalDateTime.now().getSecond());
        messageHeader.setPacketId(messageHeader.hashCode());
        messageHeader.setContentLength(0);
        return messageHeader;
    }

    @Override
    public String toString() {
        return "MessageHeader{" +
                "cmdType=" + cmdType +
                ", csType=" + csType +
                ", commandId=" + commandId +
                ", packetId=" + packetId +
                ", timeStamp=" + timeStamp +
                ", contentLength=" + contentLength +
                '}';
    }
}
