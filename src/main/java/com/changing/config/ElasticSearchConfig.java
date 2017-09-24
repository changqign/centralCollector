package com.changing.config;

import org.springframework.stereotype.Component;

/**
 * @Description :
 * @Author : wuchangqing
 * @Date : 2017/9/22
 */
@Component
public class ElasticSearchConfig {

    public ElasticSearchConfig() {}

    private String clusterNodes;

    private String clusterName;


    public String getClusterNodes() {
        return clusterNodes;
    }

    public void setClusterNodes(String clusterNodes) {
        this.clusterNodes = clusterNodes;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
