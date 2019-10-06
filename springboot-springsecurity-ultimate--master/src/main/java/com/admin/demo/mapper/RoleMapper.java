package com.admin.demo.mapper;

import com.admin.demo.entity.Department;
import com.admin.demo.entity.Menu;
import com.admin.demo.entity.Permission;
import com.admin.demo.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface RoleMapper {
    List<Role> findByUsers_Id(Long id);

    List<Role> getRoles(@Param("start") int start, @Param("size") Integer size, @Param("keywords") String keywords);

    Integer getRolesCount(String keywords);

    List<Permission> getPermission(Long id);

    List<Menu> getMenu(Long id);

    List<Department> getDept(Long id);

    int check(Role role);

    void updateRoles(Role role);

    int checkRelated(Long rid);

    int delRoles(Long rid);

    int menuRoles(@Param("rid")Long rid, @Param("mids") Long[] mids);

    void deleteMenuByRid(Long rid);

    void deletePermissionByRid(Long rid);

    int permissionRoles(@Param("rid")Long rid, @Param("pids") Long[] pids);

   int addRoles(Role role);

    void addDepts(@Param("rid")Long rid,@Param("deptIds")List<Long> deptIds);

    void delDeptByRid(Long id);

    List<Role> findByRoles(@Param("rids")List<Long> rids);


    List<Role> getAll();

    Role findByRid(Long rid);


}
