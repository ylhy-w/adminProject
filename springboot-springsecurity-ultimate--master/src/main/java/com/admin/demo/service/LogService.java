package com.admin.demo.service;

import com.admin.demo.entity.Log;
import com.admin.demo.mapper.LogMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.List;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
public interface LogService {


    /**
     * 新增日志
     * @param username
     * @param ip
     * @param joinPoint
     * @param log
     */
    @Async
    void save(String username, String ip, ProceedingJoinPoint joinPoint, Log log);



    String getErrorDetail(Long id);

    List<Log> getUserLogs(String username, int start, int size);

    List<Log> getLogs(String type, int start, int size);

    Integer getLogsCount(String type);

    Integer getUserLogsCount(String username);
}
