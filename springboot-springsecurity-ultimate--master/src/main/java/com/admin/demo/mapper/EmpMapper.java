package com.admin.demo.mapper;

import com.admin.demo.entity.Employee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EmpMapper {
    List<Employee> getEmps(@Param("start") Integer start, @Param("size")Integer size, @Param("keywords")String keywords,@Param("posId") Long posId, @Param("deptId")Long deptId,@Param("beginDate") String beginDate);

    Integer getEmpsCount(@Param("keywords")String keywords,@Param("posId") Long posId,@Param("deptId") Long deptId, @Param("beginDate")String beginDate);


}
