package com.admin.demo.service.impl;


import com.admin.common.utils.RedisUtil;
import com.admin.demo.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CacheServiceImpl implements CacheService {
    @Autowired
    private RedisUtil redisUtil;
    @Override
    public Map<String, Object> getCache(Integer page,Integer size) {
        Map<String, Object> map = new HashMap<>();
        List list=new ArrayList();
        //获取所有key
        Integer totalSize = (Integer) redisUtil.findKeysForPage("*",page,size).get("totalSize");
        List<String> keys= (List<String>) redisUtil.findKeysForPage("*",page,size).get("keys");

        for (String key : keys){
            //根据key获取相应的value
            Object value= redisUtil.get(key);
            //把value存储到list 避免map同名覆盖
            list.add(value);
        }

        map.put("key",keys);
        map.put("value",list);
        map.put("count",totalSize);
        return map;
    }

    @Override
    public boolean delCache(String key) {

        try {
            redisUtil.del(key);
        }catch (Exception e){
            return false;
        }

    return true;
    }

    @Override
    public boolean flushdb() {
        try {
            redisUtil.flushdb();
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
