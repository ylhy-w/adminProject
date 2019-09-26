package com.admin.demo.service.impl;

import com.admin.common.exception.BadRequestException;
import com.admin.demo.entity.Permission;
import com.admin.demo.mapper.PermissionMapper;
import com.admin.demo.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionMapper permissionMapper;

    //权限树
    @Override
    public Object buildTree(List<Permission> permissions) {
        List<Map<String,Object>> list = new LinkedList<>();
        permissions.forEach(permission -> {
                    if (permission!=null){
                        List<Permission> permissionList = permissionMapper.findByPid(permission.getId());
                        Map<String,Object> map = new LinkedHashMap<>();
                        map.put("id",permission.getId());
                        map.put("label",permission.getAlias());
                        map.put("pid",permission.getPid());
                        if(permissionList!=null && permissionList.size()!=0){
                            map.put("children",buildTree(permissionList));
                        }
                        list.add(map);
                    }
                }
        );
        return list;
    }

    @Override
    public List<Permission> findByPid(long l) {
        return permissionMapper.findByPid(l);
    }

    @Override
    public Object getPermission(List<Permission> permissions) {

        List<Permission> trees = new ArrayList<Permission>();
        for (Permission permission : permissions) {
            if ("0".equals(permission.getPid().toString())) {
                trees.add(permission);
            }
            for (Permission it : permissions) {
                if (it.getPid().equals(permission.getId())) {
                    if (permission.getChildren() == null) {
                        permission.setChildren(new ArrayList<Permission>());
                    }
                    permission.getChildren().add(it);
                }
            }
        }
        Integer totalElements = permissions!=null?permissions.size():0;
        Map map = new HashMap();
        map.put("content",trees.size() == 0?permissions:trees);
        map.put("totalElements",totalElements);
        return map;
    }

    @Override
    public List<Permission> findByKeywords(String keywords) {
        return permissionMapper.findByKeywords(keywords);
    }

    @Override
    public void addPermission(Permission permission) {

            if (permissionMapper.check(permission)>0){
                throw new BadRequestException("该权限已存在");
            }
        int i=   permissionMapper.addPermission(permission);
            if (i!=1){
                throw new BadRequestException("添加失败");
            }
    }

    @Override
    public void delPermission(Long id) {
        Permission per =permissionMapper.findById(id);
        if (per.getName().equals("ADMIN")){
            throw new BadRequestException("不可改动超级管理员信息");
        }
        List<Permission> permissionList=permissionMapper.findByPid(id);
        if (permissionList!=null) {
            for (Permission permission : permissionList) {
                //删除外键引用行再删除
                permissionMapper.delRolePermission(permission.getId());
                permissionMapper.delPermission(permission.getId());
            }
        }
        permissionMapper.delRolePermission(id);
        permissionMapper.delPermission(id);
    }

    @Override
    public void updatePermission(Permission permission) {
        Permission per =permissionMapper.findById(permission.getId());
        if (per.getName().equals("ADMIN")){
            throw new BadRequestException("不可改动超级管理员信息");
        }

        if (permissionMapper.check(permission)>0){
            throw new BadRequestException("该权限已存在");
        }
        permissionMapper.updatePermission(permission);
    }


}
