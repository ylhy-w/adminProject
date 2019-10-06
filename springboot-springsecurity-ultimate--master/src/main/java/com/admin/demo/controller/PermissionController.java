package com.admin.demo.controller;

import com.admin.common.exception.BadRequestException;
import com.admin.common.utils.ServerResponse;
import com.admin.demo.entity.Permission;
import com.admin.demo.service.PermissionService;
import com.admin.log.myLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("api/permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;
    //权限树
    @GetMapping("/tree")
    public ResponseEntity buildTree(){
        Object tree = permissionService.buildTree(permissionService.findByPid(0l));
        return ResponseEntity.ok(tree);
    }

    //权限查询

    @PreAuthorize("hasAnyRole('ADMIN','PERMISSIONS_ALL','PERMISSIONS_SELECT')")
    @myLog(value="查询权限")
    @GetMapping("/getPermission")
    public ResponseEntity getPermission(@RequestParam(defaultValue = "") String keywords){
        List<Permission> permissions = permissionService.findByKeywords(keywords);
        return  ResponseEntity.ok(permissionService.getPermission(permissions));
    }

    @PreAuthorize("hasAnyRole('ADMIN','PERMISSIONS_ALL','PERMISSIONS_CREATE')")
    @myLog(value="添加权限")
    @PostMapping("/addPermission")
    public ServerResponse addPermission(Permission permission){
        if (permission.getName()==null||permission.getAlias()==null){
            throw new BadRequestException("权限名称或别名为空");
        }

        permissionService.addPermission(permission);

        return ServerResponse.createBySuccessMessage("添加成功");
    }

    @PreAuthorize("hasAnyRole('ADMIN','PERMISSIONS_ALL','PERMISSIONS_DELETE')")
    @myLog(value="删除权限")
    @DeleteMapping("/delPermission/{id}")
    public ServerResponse delPermission(@PathVariable Long id){

            permissionService.delPermission(id);
        return ServerResponse.createBySuccessMessage("删除成功");
    }


    @PreAuthorize("hasAnyRole('ADMIN','PERMISSIONS_ALL','PERMISSIONS_EDIT')")
    @myLog(value="修改权限")
    @PostMapping("/updatePermission")
    public ServerResponse updatePermission(Permission permission){
       permissionService.updatePermission(permission);
        return ServerResponse.createBySuccessMessage("修改成功");
    }

}
