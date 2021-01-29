package com.xul.self.protocol.privated;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/1/2913:45
 */
public final class NettyMessage {

    private Header header;//消息头

    private Object body;//消息体

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
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
