package com.admin.demo.service.impl;


import cn.hutool.json.JSONObject;
import com.admin.common.utils.StringUtils;
import com.admin.demo.entity.Log;

import com.admin.demo.mapper.LogMapper;
import com.admin.demo.service.LogService;

import com.admin.log.myLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */
@Service
@CacheConfig(cacheNames = "logs")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LogServiceImpl implements LogService {

@Autowired
    LogMapper logMapper;

    private final String LOGINPATH = "login";




    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String username, String ip, ProceedingJoinPoint joinPoint, Log log){

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入点所在的方法
        Method method = signature.getMethod();
        myLog myLog = method.getAnnotation(com.admin.log.myLog.class);

        // 描述
        if (log != null) {
            log.setDescription(myLog.value());
        }

        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName()+"."+signature.getName()+"()";

        String params = "{";
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
        if(argValues != null){
            for (int i = 0; i < argValues.length; i++) {
                if (argNames[i].equals("password") || argNames[i].equals("repassword")){
                    argValues[i] = "******";
                }
                params += " " + argNames[i] + ": " + argValues[i];
            }
        }

        // 获取IP地址
        log.setRequestIp(ip);

        if(LOGINPATH.equals(signature.getName())){
            try {
                JSONObject jsonObject = new JSONObject(argValues[0]);
                username = jsonObject.get("username").toString();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.setAddress(StringUtils.getCityInfo(log.getRequestIp()));
        log.setMethod(methodName);
        log.setUsername(username);
        log.setParams(params + " }");
        logMapper.save(log);
    }

    @Override
    public List<Log> getLogs(String type,int start, int size,String keywords) {
        return logMapper.getLogs(type,start,size,keywords);
    }

    @Override
    public Integer getLogsCount(String type,String keywords) {
        return logMapper.getLogsCount(type,keywords);
    }

    @Override
    public Integer getUserLogsCount(String username) {
        return logMapper.getUserLogsCount(username);
    }

    @Cacheable(key = "#p0",unless="#result == null")
    @Override
    public String getErrorDetail(Long id) {
        return logMapper.getErrorDetail(id);
    }

    @Override
    public List<Log> getUserLogs(String username,int start, int size) {
        return logMapper.getUserLogs(username,start,size);
    }


}
