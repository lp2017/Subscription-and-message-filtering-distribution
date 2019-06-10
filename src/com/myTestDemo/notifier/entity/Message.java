package com.myTestDemo.notifier.entity;

/**
 * Message
 */

public class Message {

    private String type = null;

    private String sender = null;
    private String msg = null;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {

        this.sender = sender;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("  type:"+getType()).append("  sender:"+getSender()).append("  msg:"+getMsg());
        return sb.toString();
    }
}
