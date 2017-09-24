package com.changing.model;

/**
 * @Description : 解析后的格式化的日志
 * @Author : wuchangqing
 * @Date : 2017/9/22
 */
public class StructLog {

    /**
     * 时间戳
     */
    private long timeStamp;

    /**
     * 线程名，如：http-apr-8080-exec-2
     */
    private String threadName;

    /**
     * 日志级别，如：INFO
     */
    private String level;

    /**
     * logger名字，如：c.t.a.u.l.BusinessStatisticLoger
     */
    private String LoggerName;

    /**
     * 用户端输出的数据
     */
    private String data;

    /**
     * 当前机器IP信息
     */
    private String host;

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getLoggerName() {
        return LoggerName;
    }

    public void setLoggerName(String loggerName) {
        this.LoggerName = loggerName;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public StructLog timeStamp(long timeStamp){
        this.timeStamp = timeStamp;
        return this;
    }

    public StructLog threadName(String threadName){
        this.threadName = threadName;
        return this;
    }

    public StructLog level(String level){
        this.level = level;
        return this;
    }

    public StructLog LoggerName(String LoggerName){
        this.LoggerName = LoggerName;
        return this;
    }

    public StructLog data(String data){
        this.data = data;
        return this;
    }

    public StructLog host(String host){
        this.host = host;
        return this;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
