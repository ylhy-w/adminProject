package com.admin.demo.mapper;

import com.admin.demo.entity.Department;

import java.util.List;
import java.util.Set;

public interface DepartmentMapper {
    Set<Department> findByRoleIds(Long id);

    List<Department> findByPid(Long id);
}
