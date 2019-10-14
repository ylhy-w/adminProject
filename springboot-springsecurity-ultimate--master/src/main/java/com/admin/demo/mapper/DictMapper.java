package com.admin.demo.mapper;

import com.admin.demo.entity.Dict;
import com.admin.demo.entity.DictDetail;

import java.util.List;

public interface DictMapper {
    Integer getCount(String keywords);

    List<Dict> getDict(int start, Integer size, String keywords);

    Integer check(Dict dict);

    void addDict(Dict dict);

    void updateDict(Dict dict);

    void delDict(Long id);


}
