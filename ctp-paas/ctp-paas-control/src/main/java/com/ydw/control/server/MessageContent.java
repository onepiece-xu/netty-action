package com.ydw.control.server;

/**
 * @author xulh
 * @description: TODO
 * @date 2021/4/1315:41
 */
public class MessageContent {

    private String content;

    public MessageContent() {
    }

    public MessageContent(String content) {
        this.content = content;
    }

    public MessageContent(CharSequence content) {
        this.content = content.toString();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContent(CharSequence content) {
        this.content = content.toString();
    }
}
