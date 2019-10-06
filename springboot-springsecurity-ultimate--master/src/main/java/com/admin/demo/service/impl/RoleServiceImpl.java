package com.admin.demo.service.impl;

import com.admin.common.exception.BadRequestException;
import com.admin.demo.entity.Department;
import com.admin.demo.entity.Role;
import com.admin.demo.mapper.RoleMapper;
import com.admin.demo.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Transactional
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleMapper roleMapper;

    @Override
    public List<Role> findByUsers_Id(Long id) {
        return roleMapper.findByUsers_Id(id);
    }

    @Override
    public List<Role> getRoles(int start, Integer size, String keywords) {
        return roleMapper.getRoles(start,size,keywords);
    }

    @Override
    public Integer getRolesCount(String keywords) {
        return roleMapper.getRolesCount(keywords);
    }

    @Override
    public int check(Role role) {
        return roleMapper.check(role);
    }



    @Override
    public int checkRelated(Long rid) {
        return roleMapper.checkRelated(rid);
    }

    @Override
    public void delRoles(Long rid) {
        roleMapper.deleteMenuByRid(rid);
        roleMapper.delDeptByRid(rid);
        roleMapper.deletePermissionByRid(rid);
     int record =roleMapper.delRoles(rid);
     if (record==0){
         throw new BadRequestException("删除失败");
     }
    }

    @Override
    public void menuRoles(Long rid, Long[] mids) {
        roleMapper.deleteMenuByRid(rid);
        if (mids.length>0) {
            if (mids.length != roleMapper.menuRoles(rid, mids)) {
                throw new BadRequestException("更新失败");
            }
        }
    }

    @Override
    public void permissionRoles(Long rid, Long[] pids) {
        roleMapper.deletePermissionByRid(rid);
        if (pids.length>0) {
            if (pids.length != roleMapper.permissionRoles(rid, pids)) {
                throw new BadRequestException("更新失败");
            }
        }
    }


    @Override
    public void updateRoles(Role role) {

        roleMapper.delDeptByRid(role.getId());
        if (role.getDepts()!=null){
            List<Department> depts = role.getDepts();
            List<Long> deptIds = new ArrayList<Long>();
            for(Department department : depts){
                deptIds.add(department.getId());
            }
            roleMapper.addDepts(role.getId(),deptIds);
        }

        roleMapper.updateRoles(role);
    }

    @Override
    public void addRoles(Role role) {

        int record =  roleMapper.addRoles(role);
        if (role.getDepts()!=null){
            List<Department> depts = role.getDepts();
            List<Long> deptIds = new ArrayList<Long>();
            for(Department department : depts){
              deptIds.add(department.getId());
            }
            roleMapper.addDepts(role.getId(),deptIds);
        }
        if (record==0) {
            throw new BadRequestException("添加失败");
        }
    }

    @Override
    public Integer findLevel(Long userId) {
        List<Integer> levels = new ArrayList<Integer>();
      List<Role> roles = roleMapper.findByUsers_Id(userId);
        for(Role role : roles){
            levels.add(role.getLevel());
        }
        return  Collections.min(levels);
    }

    @Override
    public Integer findByRoles(List<Role> roles) {
        List<Integer> levels = new ArrayList<Integer>();
        List<Long> rids = new ArrayList<Long>();
        for(Role role : roles){
            rids.add(role.getId());
        }

       List<Role> roleList=roleMapper.findByRoles(rids);
        for(Role role : roleList){
            levels.add(role.getLevel());
        }

        return Collections.min(levels);
    }

    @Override
    public List<Role> getAll() {
        return roleMapper.getAll();
    }

    @Override
    public Role findByRid(Long rid) {
        return roleMapper.findByRid(rid);
    }





}
