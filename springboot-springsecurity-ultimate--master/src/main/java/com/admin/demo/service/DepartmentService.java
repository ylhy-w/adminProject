package com.admin.demo.service;

import com.admin.demo.entity.Department;
import com.admin.demo.entity.QueryVo;

import java.util.List;
import java.util.Set;

public interface DepartmentService {
    Set<Department> findByRoleIds(Long id);

    List<Department> findByPid(Long id);

    Object  buildTree(List<Department> departments);

    List<Department> query(QueryVo queryVo);

    Object getDept(List<Department> departments);

    void addDept(Department department);

    void delDept(Long id);

    void updateDept(Department department);

}
