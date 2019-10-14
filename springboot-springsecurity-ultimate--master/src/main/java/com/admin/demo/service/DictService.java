package com.admin.demo.service;

import com.admin.demo.entity.Dict;

import java.util.Map;

public interface DictService {
    Map<String,Object> getDict(int start, Integer size, String keywords);

    void addDict(Dict dict);

    void updateDict(Dict dict);

    void delDict(Long id);
}
