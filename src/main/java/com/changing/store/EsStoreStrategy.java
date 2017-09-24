package com.changing.store;

import com.changing.dao.ElasticSearchDao;
import com.changing.model.Message;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Description : ES存储策略
 * @Author : wuchangqing
 * @Date : 2017/9/22
 */
@Component
public class EsStoreStrategy implements StoreStrategy{

    @Resource
    private ElasticSearchDao elasticSearchDao;

    @Override
    public void store(Message message) {
        elasticSearchDao.insert(message);
    }

}
