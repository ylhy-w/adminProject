package com.admin.demo.controller;

import com.admin.common.utils.SecurityUtils;
import com.admin.demo.entity.Log;
import com.admin.demo.service.LogService;
import com.admin.log.myLog;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@RestController
@RequestMapping("logs")
public class LogController {

    @Autowired
    private LogService logService;
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @GetMapping("/info")
    public ResponseEntity getLogs(@RequestParam(defaultValue = "1") int page,
                                  @RequestParam(defaultValue = "10") int size){
        int start= (page-1)*size;
        Map<String,Object> map=new HashMap<>(2);
        String type = "INFO";
        List<Log> logs = logService.getLogs(type,start,size);
        Integer count = logService.getLogsCount(type);
        map.put("logs",logs);
        map.put("count",count);
        return   ResponseEntity.ok(map);
    }


    @GetMapping("/error")
    public ResponseEntity getErrorLogs(@RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "10") int size){
        int start= (page-1)*size;
        String type = "ERROR";
        Map<String, Object> map = new HashMap<>(2);
        List<Log> logs = logService.getLogs(type,start,size);
        Integer count = logService.getLogsCount(type);
        map.put("logs",logs);
        map.put("count",count);
        return   ResponseEntity.ok(map);
    }


    @GetMapping("/error/{id}")
    public ResponseEntity getErrorLogsById(@PathVariable Long id){
        Map<String,Object> map=new HashMap<>(1);
        String detail = logService.getErrorDetail(id);
        map.put("detail",detail);
        return   ResponseEntity.ok(map);
    }

    @GetMapping("/user")
    public ResponseEntity getUserLogs(@RequestParam(defaultValue = "1") int page,
                                      @RequestParam(defaultValue = "10") int size){
        int start= (page-1)*size;
        Map<String, Object> map = new HashMap<>(2);
        List<Log> logs = logService.getUserLogs(SecurityUtils.getUsername(),start,size);
        Integer count = logService.getUserLogsCount(SecurityUtils.getUsername());
        map.put("logs",logs);
        map.put("count",count);
        return   ResponseEntity.ok(map);
    }
}
