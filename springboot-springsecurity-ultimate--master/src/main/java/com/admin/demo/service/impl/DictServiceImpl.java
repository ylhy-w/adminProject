package com.admin.demo.service.impl;

import com.admin.common.exception.BadRequestException;
import com.admin.demo.entity.Dict;
import com.admin.demo.entity.DictDetail;
import com.admin.demo.mapper.DictDetailMapper;
import com.admin.demo.mapper.DictMapper;
import com.admin.demo.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class DictServiceImpl implements DictService {
    @Autowired
    DictMapper dictMapper;
    @Autowired
    DictDetailMapper dictDetailMapper;
    @Override
    public Map<String, Object> getDict(int start, Integer size, String keywords) {
        Map<String, Object> map=new HashMap<>();
        Integer count=dictMapper.getCount(keywords);
        List<Dict> dictList = dictMapper.getDict(start,size,keywords);
        map.put("count",count);
        map.put("dictList",dictList);
        return map;

    }

    @Override
    public void addDict(Dict dict) {
        Integer i = dictMapper.check(dict);
        if (i==0){
            dictMapper.addDict(dict);
        }else {
            throw  new BadRequestException("该字典已存在");
        }


    }

    @Override
    public void updateDict(Dict dict) {
        Integer i = dictMapper.check(dict);
        if (i==0){
            dictMapper.updateDict(dict);
        }else {
            throw  new BadRequestException("该字典已存在");
        }
    }

    @Override
    public void delDict(Long id) {
        List<DictDetail> dictDetailList = dictDetailMapper.findBydictId(id);
    for (DictDetail dictDetail: dictDetailList){
        dictDetailMapper.delDictDetail(dictDetail.getId());
    }
        dictMapper.delDict(id);
    }
}
