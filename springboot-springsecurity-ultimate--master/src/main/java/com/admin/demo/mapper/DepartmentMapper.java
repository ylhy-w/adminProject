package com.admin.demo.mapper;

import com.admin.demo.entity.Department;
import com.admin.demo.entity.QueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface DepartmentMapper {
    Set<Department> findByRoleIds(Long id);

    List<Department> findByPid(Long id);

    List<Department> query(QueryVo queryVo);

    Integer check(Department department);

    void addDept(Department department);

    void delDept( Long id);

    Integer checkRelated(Long id);

    void delRoleDept(Long id);

    void updateDept(Department department);

}
