package com.admin.demo.service;

import java.util.Map;

public interface CacheService {

    Map<String, Object> getCache(Integer page, Integer size);

    boolean delCache(String... key);

    boolean flushdb();
}
