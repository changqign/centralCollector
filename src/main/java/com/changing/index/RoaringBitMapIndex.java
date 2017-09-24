package com.changing.index;

import com.changing.model.db.LogInfos;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.roaringbitmap.RoaringBitmap;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Description :
 * @Author : wuchangqing
 * @Date : 2017/9/23
 */
@Component
public class RoaringBitMapIndex implements IndexStrategy {

    private Map<String,RoaringBitmap> indexes = new ConcurrentHashMap<String, RoaringBitmap>();

    private static final String INDEX_ALL_KEY = "";

    {
        RoaringBitmap all = new RoaringBitmap();
        indexes.put(INDEX_ALL_KEY, all);
    }

    public void index(LogInfos logInfos) {
        Integer id = logInfos.getId();
        if(id == null){
            return;
        }
        String data = logInfos.getData();
        List<String> words = splitWords(data);
        for (String word : words) {
            indexWord(id,word);
        }
    }

    public void batchIndex(List<LogInfos> logInfosList) {
        for (LogInfos logInfos : logInfosList) {
            index(logInfos);
        }
    }

    public List<Integer> search(String queryString, List<Integer> ids) {
        //注意，在上层控制，传到这里的queryString只是data字段的内容
        List<String> words = splitWords(queryString);
        for (String word : words) {
            //TODO
        }

        return null;
    }

    public void indexWord(int id, String word){
        RoaringBitmap bitMap = indexes.get(word);
        if(bitMap == null){
            synchronized (this){
                bitMap = indexes.get(word);
                if(bitMap == null){
                    bitMap = new RoaringBitmap();
                    indexes.put(word,bitMap);
                }
            }
        }
        bitMap.add(id);
        //记录下全量数据
        indexes.get(INDEX_ALL_KEY).add(id);
    }


    public List<String> splitWords(String queryString){
        List<String> list =
                Lists.newArrayList(queryString.split(" ")).stream().filter(s -> StringUtils.isNotBlank(s)).distinct()
                        .collect(Collectors.toList());
        return list;
    }


}
