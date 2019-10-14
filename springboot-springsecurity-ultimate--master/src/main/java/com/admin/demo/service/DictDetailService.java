package com.admin.demo.service;

import com.admin.demo.entity.DictDetail;

import java.util.Map;

public interface DictDetailService {

    Map<String,Object> getDictDetails(int start, Integer size, Long dict_id);

    Map<String,Object> getDetailsMap(int start, Integer size, Long dict_id, String keywords);

    void delDictDetail(Long id);

    void addDetail(DictDetail dictDetail);

    void updateDetail(DictDetail dictDetail);
}
