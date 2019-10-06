package com.admin.demo.controller;

import com.admin.common.utils.SecurityUtils;
import com.admin.demo.entity.Menu;

import com.admin.demo.service.MenuService;
import com.admin.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/menu")
public class MenuController {

    @Autowired
    MenuService menuService;
    @Autowired
    UserService userService;
    //初始化菜单
    @GetMapping(value = "/build")
    public ResponseEntity buildMenus(){
     List<Menu> menuTree = menuService.buildTree(SecurityUtils.getUserId());
        return new ResponseEntity(menuTree, HttpStatus.OK);
    }



    @GetMapping(value = "/tree")
    public ResponseEntity menusTree(){
        Object menuTree =menuService.menusTree(menuService.findByPid(0l));
        return new ResponseEntity(menuTree, HttpStatus.OK);
    }
}
