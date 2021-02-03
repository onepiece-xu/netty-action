package com.op.model;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/1/2913:45
 */
public final class NettyMessage {

    private NettyHeader header;//消息头

    private Object body;//消息体

    public NettyHeader getHeader() {
        return header;
    }

    public void setHeader(NettyHeader header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "NettyMessage{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }
}
