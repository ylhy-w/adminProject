package com.admin.demo.service.impl;

import com.admin.common.exception.BadRequestException;
import com.admin.demo.entity.DictDetail;
import com.admin.demo.mapper.DictDetailMapper;
import com.admin.demo.service.DictDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DictDetailServiceImpl implements DictDetailService {
    @Autowired
    DictDetailMapper dictDetailMapper;
    @Override
    public Map<String, Object> getDictDetails(int start, Integer size, Long dict_id) {
        Map<String, Object> map=new HashMap<>();
        Integer count=dictDetailMapper.getCount(dict_id);
        List<DictDetail> dictDetailList = dictDetailMapper.getDictDetail(start,size,dict_id);
        map.put("count",count);
        map.put("dictDetailList",dictDetailList);
        return map;

    }

    @Override
    public Map<String, Object> getDetailsMap(int start, Integer size, Long dict_id, String laber) {
        Map<String, Object> map=new HashMap<>();
        Integer count=dictDetailMapper.getDetailsMapCount(dict_id,laber);
        List<DictDetail> dictDetailList = dictDetailMapper.getDetailsMap(start,size,dict_id,laber);
        map.put("count",count);
        map.put("dictDetailList",dictDetailList);
        return map;
    }

    @Override
    public void delDictDetail(Long id) {
        dictDetailMapper.delDictDetail(id);
    }

    @Override
    public void addDetail(DictDetail dictDetail) {
        Integer i = dictDetailMapper.check(dictDetail);
        if (i==0){
            dictDetailMapper.addDetail(dictDetail);
        }else {
            throw  new BadRequestException("该字典标签已存在");
        }
    }

    @Override
    public void updateDetail(DictDetail dictDetail) {
        Integer i = dictDetailMapper.check(dictDetail);
        if (i==0){
            dictDetailMapper.updateDetail(dictDetail);
        }else {
            throw  new BadRequestException("该字典标签已存在");
        }
    }
}
