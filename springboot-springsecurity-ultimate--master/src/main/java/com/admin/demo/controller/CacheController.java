package com.admin.demo.controller;

import com.admin.common.exception.BadRequestException;
import com.admin.common.utils.ServerResponse;
import com.admin.demo.service.CacheService;
import com.admin.log.myLog;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/cache")
public class CacheController {


    @Autowired
    CacheService cacheService;

    //显示redis缓存
    @myLog(value = "查看redis缓存")
    @GetMapping("/getCache")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_SELECT')")
    public Map<String, Object> getCache(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size){
        return cacheService.getCache(page,size);
    }

    //删除redis缓存
    @myLog(value = "删除redis缓存")
    @DeleteMapping("/delCache")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
    public ServerResponse delCache(String key){
        if (cacheService.delCache(key)) {
            return ServerResponse.createBySuccessMessage("删除成功");
        }
     throw new BadRequestException("删除失败");
    }

    @myLog("清空Redis缓存")
    @DeleteMapping(value = "/delAll")
    @PreAuthorize("hasAnyRole('ADMIN','REDIS_ALL','REDIS_DELETE')")
    public ServerResponse deleteAll(){
        if (cacheService.flushdb()) {
            return ServerResponse.createBySuccessMessage("清空成功");
        }
        throw new BadRequestException("清空失败");
    }
}
