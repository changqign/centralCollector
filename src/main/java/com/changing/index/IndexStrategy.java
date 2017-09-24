package com.changing.index;

import com.changing.model.db.LogInfos;

import java.util.List;

/**
 * @Description :
 * @Author : wuchangqing
 * @Date : 2017/9/24
 */
public interface IndexStrategy {

    void index(LogInfos logInfos);

    void batchIndex(List<LogInfos>logInfosList);

    List<Integer> search(String queryString , List<Integer> ids);
}
