package com.admin.demo.controller;

import com.admin.demo.entity.Department;
import com.admin.demo.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("dept")
public class DeptController {
    @Autowired
    DepartmentService departmentService;
    //部门树
    @GetMapping(value = "/tree")
    public ResponseEntity buildMenus(){
        Object deptTree = departmentService.buildTree(departmentService.findByPid(0l));
        return new ResponseEntity(deptTree, HttpStatus.OK);
    }
}
