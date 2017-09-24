package com.changing.store;

import com.changing.model.Message;

/**
 * @Description : 存储策略
 * @Author : wuchangqing
 * @Date : 2017/9/22
 */
public interface StoreStrategy {

    void store(Message message);

}
