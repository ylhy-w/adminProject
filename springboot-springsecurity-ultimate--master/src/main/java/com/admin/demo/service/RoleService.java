package com.admin.demo.service;

import com.admin.demo.entity.Role;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    List<Role> findByUsers_Id(Long id);

    List<Role> getRoles(int start, Integer size, String keywords);

    Integer getRolesCount(String keywords);

    int check(Role role);

    void updateRoles(Role role);

    int checkRelated(Long rid);

    void delRoles(Long rid);

    void menuRoles(Long rid, Long[] mids);

    void permissionRoles(Long rid, Long[] pids);

    void addRoles(Role role);

    Integer findLevel(Long userId);

    Integer findByRoles(List<Role> roles);


    List<Role> getAll();

    Role findByRid(Long rid);

}
