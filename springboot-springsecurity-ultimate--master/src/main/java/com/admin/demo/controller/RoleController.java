package com.admin.demo.controller;


import com.admin.common.exception.BadRequestException;
import com.admin.common.utils.SecurityUtils;
import com.admin.common.utils.ServerResponse;
import com.admin.demo.entity.Role;
import com.admin.demo.service.RoleService;
import com.admin.log.myLog;
import com.mysql.cj.x.protobuf.MysqlxCrud;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("role")
@RestController
public class RoleController {
    @Autowired
    RoleService roleService;

    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_SELECT')")
    @myLog(value="查询角色")
    @GetMapping(value = "/getRoles")
    public ResponseEntity getRoles(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(defaultValue = "") String keywords){
        int start = (page-1)*size;
        Map<String, Object> map = new HashMap<>();
        List<Role> roles = roleService.getRoles(start,size,keywords);
        Integer count = roleService.getRolesCount(keywords);
        map.put("roles",roles);
        map.put("count",count);
        return ResponseEntity.ok(map);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    @myLog(value="修改角色")
    @PostMapping(value = "/updateRoles")
    public ServerResponse updateRoles(@RequestBody Role role){
        int i=roleService.check(role);
            if (i>0)
                throw new BadRequestException("该角色名已存在");
                roleService.updateRoles(role);
        return  ServerResponse.createBySuccess("修改成功",role);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_CREATE')")
    @myLog(value="添加角色")
    @PostMapping(value = "/addRoles")
    public ServerResponse addRoles(@RequestBody Role role){

        if ( role.getName()==null){
            throw new BadRequestException("角色名为空");
        }
        int i=roleService.check(role);
        if (i>0)
            throw new BadRequestException("该角色名已存在");

        Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        role.setCreateTime(date);
        roleService.addRoles(role);
           return ServerResponse.createBySuccess("添加成功", role);

    }

    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_DELETE')")
    @myLog(value="删除角色")
    @DeleteMapping(value = "/delRoles/{rid}")
    public ServerResponse delRoles(@PathVariable Long rid){
        int i=roleService.checkRelated(rid);
        if (i>0)
            throw new BadRequestException("该角色存在用户关联");
      roleService.delRoles(rid);
            return ServerResponse.createBySuccessMessage("删除成功");
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    @myLog(value="角色菜单分配")
    @PostMapping(value = "/menuRoles")
    public ServerResponse menuRoles(Long rid, Long[] mids){
        roleService.menuRoles(rid, mids);
        return ServerResponse.createBySuccessMessage("更新成功!");
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT')")
    @myLog(value="角色权限分配")
    @PostMapping(value = "/permissionRoles")
    public ServerResponse permissionRoles(Long rid, Long[] pids){
        roleService.permissionRoles(rid, pids);
        return ServerResponse.createBySuccessMessage("更新成功!");
    }



    @GetMapping(value = "/level")
    public ResponseEntity getLevel(){
        Map<String,Object> map = new HashMap<>();
        Integer levels = roleService.findLevel(SecurityUtils.getUserId());
        map.put("level",levels);
        return  ResponseEntity.ok(map);
    }


    //用户管理角色下拉列表
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT','USER_CREATE','USER_SELECT','USER_DELETE')")
    @GetMapping(value = "/all")
        public ResponseEntity getRoles(){
        List<Role> roles = roleService.getAll();
        return ResponseEntity.ok(roles);
    }

    @PreAuthorize("hasAnyRole('ADMIN','ROLES_ALL','ROLES_EDIT','ROLES_CREATE','ROLES_SELECT','ROLES_DELETE')")
    @GetMapping(value = "/{rid}")
    public ResponseEntity getRoles(@PathVariable Long rid){
     Role role = roleService.findByRid(rid);
        return ResponseEntity.ok(role);
    }

}
