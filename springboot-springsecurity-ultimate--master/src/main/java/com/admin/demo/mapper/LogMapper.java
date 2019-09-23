package com.admin.demo.mapper;

import com.admin.demo.entity.Log;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LogMapper {
    void save(Log log);

    List<Log> getLogs(@Param("logType")String type,@Param("start")  int start, @Param("size") int size);

    String getErrorDetail(Long id);

    List<Log> getUserLogs(@Param("username") String username,@Param("start")  int start, @Param("size") int size);

    Integer getLogsCount(String type);

    Integer getUserLogsCount(String username);
}
