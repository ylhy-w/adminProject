package com.admin.common.config;


import com.admin.common.utils.SecurityUtils;

import com.admin.demo.entity.Department;
import com.admin.demo.entity.Role;
import com.admin.demo.entity.User;
import com.admin.demo.service.DepartmentService;
import com.admin.demo.service.RoleService;
import com.admin.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据权限配置
 * @author Zheng Jie
 * @date 2019-4-1
 */
@Component
public class DataScope {

    private final String[] scopeType = {"全部","本级","自定义"};

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DepartmentService departmentService;


    public Set<Long> getDeptIds() {

        User user = (User) userService.loadUserByUsername(SecurityUtils.getUsername());

        // 用于存储部门id
        Set<Long> deptIds = new HashSet<>();

        // 查询用户角色
        List<Role> roleSet = roleService.findByUsers_Id(user.getId());;


        //角色数据级别
        for (Role role : roleSet) {
//全部
            if (scopeType[0].equals(role.getDataScope())) {
            //    System.out.println("全部");
                return new HashSet<>() ;
            }

            // 存储本级的数据权限
            if (scopeType[1].equals(role.getDataScope())) {
         //       System.out.println("本级");
                //本级取决于user的deptId
                deptIds.add(user.getDeptId());

            }

            // 存储自定义的数据权限
            if (scopeType[2].equals(role.getDataScope())) {
            //    System.out.println("自定义");
                Set<Department> depts = departmentService.findByRoleIds(role.getId());
                for (Department dept : depts) {
                    deptIds.add(dept.getId());
                    List<Department> deptChildren = departmentService.findByPid(dept.getId());
                    if (deptChildren != null && deptChildren.size() != 0) {
                        deptIds.addAll(getDeptChildren(deptChildren));
                    }
                }

            }
        }

        return deptIds;
    }


    //子部门
    public List<Long> getDeptChildren(List<Department> deptList) {
        List<Long> list = new ArrayList<>();
        deptList.forEach(dept -> {
                    if (dept!=null && dept.getEnabled()){
                        List<Department> depts = departmentService.findByPid(dept.getId());
                        if(deptList!=null && deptList.size()!=0){
                            list.addAll(getDeptChildren(depts));
                        }
                        list.add(dept.getId());
                    }
                }
        );
        return list;
    }
}
