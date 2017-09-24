package com.changing.model;

import java.util.List;

/**
 * @Description : Message信息
 * @Author : wuchangqing
 * @Date : 2017/9/22
 */
public class Message {

    private List<StructLog> logs;

    public List<StructLog> getLogs() {
        return logs;
    }

    public void setLogs(List<StructLog> logs) {
        this.logs = logs;
    }

    public Message() {
    }

    public Message(List<StructLog> logs) {
        this.logs = logs;
    }
}
