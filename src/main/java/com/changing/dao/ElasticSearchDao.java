package com.changing.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.changing.config.ElasticSearchConfig;
import com.changing.model.Message;
import com.changing.model.StructLog;
import com.changing.util.Constants;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

/**
 * @Description : Es的数据操作
 * @Author : wuchangqing
 * @Date : 2017/9/22
 */
@Repository("elasticSearchDao")
@DependsOn("elasticSearchConfig")
public class ElasticSearchDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticSearchDao.class);

    @Resource
    ElasticSearchConfig elasticSearchConfig;

    TransportClient client = null;

    @PostConstruct
    private void initElasticSearchDao() {
        LOGGER.info("initElasticSearchDao");
        String name = elasticSearchConfig.getClusterName();
        String nodes = elasticSearchConfig.getClusterNodes();

        if (StringUtils.isNotBlank(name)  && StringUtils.isNotBlank(nodes)) {
            Settings setting =
                    Settings.builder().put("cluster.name", name).put("client.transport.sniff", true).build();
            JSONArray nodeArray = JSONArray.parseArray(nodes);

            List<InetSocketTransportAddress> transportAddressList = Lists.newArrayList();
            for (int i = 0; i < nodeArray.size(); i++) {
                try {
                    JSONObject obj = nodeArray.getJSONObject(i);
                    InetSocketTransportAddress transportAddress = new InetSocketTransportAddress(
                            InetAddress.getByName(obj.getString("name")), obj.getInteger("port"));
                    transportAddressList.add(transportAddress);
                } catch (Exception e) {
                    LOGGER.error("initElasticSearchDao add transportAddress error ", e);
                }
            }
            if (!transportAddressList.isEmpty()) {
                client = new PreBuiltTransportClient(setting);
                for (InetSocketTransportAddress node : transportAddressList) {
                    client.addTransportAddresses(node);
                }
            }
        }
    }

    public TransportClient getClient() {
        return client;
    }

    public void insert(Message message){
        //批量处理
        BulkRequestBuilder bulkRequest = client.prepareBulk();
        for (StructLog structLog : message.getLogs()) {
            IndexRequest indexRequest = new IndexRequest(Constants.EsInfos.ES_INDEX , Constants.EsInfos.ES_TYPE);
            indexRequest.source(toXContentBuilder(structLog));
            bulkRequest.add(indexRequest);
        }
        bulkRequest.execute(new ActionListener<BulkResponse>() {
            public void onResponse(BulkResponse bulkItemResponses) {
                LOGGER.info("bulk insert execute success , count is {} , timeCost is {} ms ",bulkItemResponses.getItems().length,bulkItemResponses.getIngestTookInMillis());
            }

            public void onFailure(Exception e) {
                LOGGER.error("bulk insert execute error" , e);
            }
        });
    }


    public XContentBuilder toXContentBuilder(StructLog structLog) {
        XContentBuilder jsonBuild = null;
        try {
            jsonBuild = XContentFactory.jsonBuilder();
            jsonBuild.startObject();
            jsonBuild.field("dateTime",structLog.getTimeStamp());
            jsonBuild.field("level",structLog.getLevel());
            jsonBuild.field("host",structLog.getHost());
            jsonBuild.field("loggerName",structLog.getLoggerName());
            jsonBuild.field("threadName",structLog.getThreadName());
            jsonBuild.field("data",structLog.getData());
            jsonBuild.endObject();
        } catch (IOException e) {
           LOGGER.error("toXContentBuilder error", e);
        }
        return jsonBuild;
    }

}
