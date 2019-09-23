package com.admin.demo.service.impl;

import com.admin.demo.entity.Employee;
import com.admin.demo.mapper.EmpMapper;
import com.admin.demo.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class EmpServiceImpl implements EmpService {

    @Autowired
    EmpMapper empMapper;
    @Override
    public List<Employee> getEmps(Integer page, Integer size, String keywords, Long posId, Long deptId, String beginDate) {
        int start = (page - 1) * size;
        return empMapper.getEmps(start,size,keywords,posId,deptId,beginDate);
    }

    @Override
    public Long getEmpsCount(String keywords, Long posId, Long deptId, String beginDate) {
        return empMapper.getEmpsCount(keywords,posId,deptId,beginDate);
    }

    @Override
    public Long getMaxWorkId() {
        Long maxWorkID = empMapper.getMaxWorkID();
        return maxWorkID == null ? 0 : maxWorkID;
    }


}
