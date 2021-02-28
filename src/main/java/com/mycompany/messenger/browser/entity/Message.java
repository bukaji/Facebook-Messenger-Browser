package com.mycompany.messenger.browser.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringJoiner;

/**
 *
 * @author bukja
 */
public class Message {

    private String senderName;
    private Date timestampMs;
    private String content;
    private String type;
    private Boolean isUnsent;

    public Message() {
    }

    public Message(String senderName, Date timestampMs, String content, String type, Boolean isUnsent) {
        this.senderName = senderName;
        this.timestampMs = timestampMs;
        this.content = content;
        this.type = type;
        this.isUnsent = isUnsent;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public Date getTimestampMs() {
        return timestampMs;
    }

    public void setTimestampMs(Date timestampMs) {
        this.timestampMs = timestampMs;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsUnsent() {
        return isUnsent;
    }

    public void setIsUnsent(Boolean isUnsent) {
        this.isUnsent = isUnsent;
    }

    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        String dateString = format.format(timestampMs);

        return new StringJoiner(" ")
                .add(dateString)
                .add(senderName + ":")
                .add(content)
                .toString();
    }

}
