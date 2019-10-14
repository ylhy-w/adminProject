package com.admin.demo.service;

import com.admin.demo.entity.Employee;

import java.util.List;

public interface EmpService {
    List<Employee> getEmps(Integer page, Integer size, String keywords, Long posId, Long deptId, String beginDate);

    Integer getEmpsCount(String keywords, Long posId, Long deptId, String beginDate);




}
