package com.admin.demo.service.impl;

import com.admin.demo.entity.Department;
import com.admin.demo.mapper.DepartmentMapper;
import com.admin.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    public Set<Department> findByRoleIds(Long id) {
        return departmentMapper.findByRoleIds(id);
    }

    @Override
    public List<Department> findByPid(Long id) {
        return departmentMapper.findByPid(id);
    }

    @Override
    public Object buildTree(List<Department> departments) {
        List<Map<String,Object>> list = new LinkedList<>();
        departments.forEach(department -> {
                    if (department!=null){
                        List<Department> departmentList = departmentMapper.findByPid(department.getId());
                        //            LinkedHashMap 按添加顺序保存数据
                        Map<String,Object> map = new LinkedHashMap<>();
                        map.put("id",department.getId());
                        map.put("label",department.getName());
                        map.put("pid",department.getPid());
                        if(departmentList!=null && departmentList.size()!=0){
                            map.put("children",buildTree(departmentList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }

}
