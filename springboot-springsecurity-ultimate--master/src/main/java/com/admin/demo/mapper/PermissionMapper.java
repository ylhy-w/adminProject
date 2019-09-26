package com.admin.demo.mapper;

import com.admin.demo.entity.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PermissionMapper {
    List<Permission> buildTree(Integer[] ids);

    Integer[] getAllPermission();

    List<Permission> findByPid(long l);

    List<Permission> findByKeywords(@Param("keywords") String keywords);

    int check(Permission permission);

    int addPermission(Permission permission);

    void delPermission(Long id);

    void delRolePermission(Long id);

    void updatePermission(Permission permission);

    Permission findById(Long id);
}
