package com.changing.store;

import com.changing.dao.mapper.LogInfosMapper;
import com.changing.index.IndexStrategy;
import com.changing.model.Message;
import com.changing.model.StructLog;
import com.changing.model.db.LogInfos;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description :
 * @Author : wuchangqing
 * @Date : 2017/9/23
 */
@Component
public class DbStoreStrategy implements StoreStrategy {

    private static final int PARTITION_SIZE = 200;

    @Resource
    private LogInfosMapper logInfosMapper;

    @Resource
    private IndexStrategy indexStrategy;

    public void store(Message message) {
        //DB 批量存储
        List<StructLog> logs = message.getLogs();
        int size = logs.size();
        int times = size % PARTITION_SIZE == 0 ? size / PARTITION_SIZE : size / PARTITION_SIZE + 1;
        for (int i = 0; i < times; i++) {
            int end = (i+1) * PARTITION_SIZE;
            if(end > size){
                end = size;
            }
            List<StructLog> subStructLogs = logs.subList(i * PARTITION_SIZE, end);
            //批量插入，并确保插入后的ID作为bitmap的操作位
            List<LogInfos> logInfosList = convertStructLogsToLogInfos(subStructLogs);
            logInfosMapper.insertBatch(logInfosList);
            //返回的数据ID是存在的，索引操作
            indexStrategy.batchIndex(logInfosList);
        }
    }


    public List<LogInfos> convertStructLogsToLogInfos(List<StructLog> structLogs){
        List<LogInfos> logInfosList = new LinkedList<LogInfos>();
        for (StructLog structLog : structLogs) {
            LogInfos logInfos = new LogInfos().logTime(structLog.getTimeStamp()).data(structLog.getData()).host(structLog.getHost()).level(structLog.getLevel()).loggerName(structLog.getLoggerName()).threadName(structLog.getLoggerName());
            logInfosList.add(logInfos);
        }
        return logInfosList;
    }


}
