package com.admin.demo.service;

import com.admin.demo.entity.Permission;

import java.util.List;

public interface PermissionService {
    Object buildTree(List<Permission> permissions);

    List<Permission> findByPid(long l);

    Object getPermission(List<Permission> permissions);

    List<Permission> findByKeywords(String keywords);

    void addPermission(Permission permission);

    void delPermission(Long id);

    void updatePermission(Permission permission);
}
