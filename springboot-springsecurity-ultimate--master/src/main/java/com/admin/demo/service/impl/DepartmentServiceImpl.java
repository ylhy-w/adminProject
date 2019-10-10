package com.admin.demo.service.impl;

import com.admin.common.exception.BadRequestException;
import com.admin.demo.entity.Department;
import com.admin.demo.entity.QueryVo;
import com.admin.demo.mapper.DepartmentMapper;
import com.admin.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
@CacheConfig(cacheNames = "dept")
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    DepartmentMapper departmentMapper;

    @Override
    public Set<Department> findByRoleIds(Long id) {
        return departmentMapper.findByRoleIds(id);
    }

    @Cacheable(key = "#p0",unless="#result == null")
    @Override
    public List<Department> findByPid(Long id) {
        return departmentMapper.findByPid(id);
    }

    @Cacheable(key = "#root.targetClass.simpleName+':'+#root.methodName",unless="#result == null")
    @Override
    public Object buildTree(List<Department> departments) {
        List<Map<String,Object>> list = new LinkedList<>();
        departments.forEach(department -> {
                    if (department!=null){
                        List<Department> departmentList = departmentMapper.findByPid(department.getId());
                        //            LinkedHashMap 按添加顺序保存数据
                        Map<String,Object> map = new LinkedHashMap<>();
                        if (department.getEnabled()) {
                            map.put("id", department.getId());
                            map.put("label", department.getName());
                            map.put("pid", department.getPid());
                            if (departmentList != null && departmentList.size() != 0) {
                                map.put("children", buildTree(departmentList));
                            }
                        list.add(map);
                        }
                    }
                }
        );
        return list;
    }

    @Override
    @Cacheable(key = "#root.targetClass.simpleName+':'+#root.methodName+':'+#root.args[0] ",unless="#result == null")
    public List<Department> query(QueryVo queryVo) {
        return departmentMapper.query(queryVo);
    }

    @Cacheable(key = "#root.targetClass.simpleName+':'+#root.methodName ",unless="#result == null")
    @Override
    public Object getDept(List<Department> departments) {
        List<Department> trees = new ArrayList<Department>();
        for (Department department : departments) {
            if ("0".equals(department.getPid().toString())) {
                trees.add(department);
            }
            for (Department it : departments) {
                if (it.getPid().equals(department.getId())) {
                    if (department.getChildren() == null) {
                        department.setChildren(new ArrayList<Department>());
                    }
                    department.getChildren().add(it);
                }
            }
        }
        Integer totalElements = departments!=null?departments.size():0;
        Map map = new HashMap();
        map.put("content",trees.size() == 0?departments:trees);
        map.put("totalElements",totalElements);
        return map;
    }


    @Caching(
            evict = {
                    @CacheEvict(value = "dept",allEntries=true),
            }
    )
    @Override
    public void addDept(Department department) {
        Integer i = departmentMapper.check(department);
        if (i>0){
            throw new BadRequestException("该部门已存在");
        }

        departmentMapper.addDept(department);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "dept",allEntries=true),
            }
    )
    @Override
    public void delDept(Long id) {
        Integer i = departmentMapper.checkRelated(id);
        departmentMapper.delRoleDept(id);

        List<Department> departments = departmentMapper.findByPid(id);
                if(departments.size()>0){
                    throw new BadRequestException("该部门存在子部门，请先删除下级部门");
                }

        if (i>0){
            throw new BadRequestException("该部门存在用户关联或职位关联，不能删除");
        }

        departmentMapper.delDept(id);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "dept",allEntries=true),
            }
    )
    @Override
    public void updateDept(Department department) {
        Integer i = departmentMapper.check(department);
        if (i>0){
            throw new BadRequestException("该部门已存在");
        }
        departmentMapper.updateDept(department);
    }

}


