package com.admin.demo.mapper;


import com.admin.demo.entity.DictDetail;

import java.util.List;

public interface DictDetailMapper {
    Integer getCount(Long dict_id);

    List<DictDetail> getDictDetail(int start, Integer size, Long dict_id);

    Integer getDetailsMapCount(Long dict_id, String keywords);

    List<DictDetail> getDetailsMap(int start, Integer size, Long dict_id, String keywords);

    List<DictDetail> findBydictId(Long id);

    void delDictDetail(Long id);

    Integer check(DictDetail dictDetail);

    void addDetail(DictDetail dictDetail);

    void updateDetail(DictDetail dictDetail);
}
