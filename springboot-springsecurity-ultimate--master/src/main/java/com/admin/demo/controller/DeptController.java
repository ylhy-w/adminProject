package com.admin.demo.controller;

import com.admin.common.utils.ServerResponse;
import com.admin.demo.entity.Department;
import com.admin.demo.entity.QueryVo;
import com.admin.demo.service.DepartmentService;
import com.admin.log.myLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/dept")
public class DeptController {
    @Autowired
    DepartmentService departmentService;
    //部门树
    @GetMapping(value = "/tree")
    public ResponseEntity buildMenus(){
        Object deptTree = departmentService.buildTree(departmentService.findByPid(0l));
        return new ResponseEntity(deptTree, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_SELECT')")
    @myLog(value="查询部门")
    @GetMapping("/getDept")
    public ResponseEntity getDept(QueryVo queryVo){
        List<Department> departments = departmentService.query(queryVo);
        return  ResponseEntity.ok(departmentService.getDept(departments));
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_CREATE')")
    @myLog(value="添加部门")
    @PostMapping("/addDept")
    public ServerResponse addDept(Department department){
        departmentService.addDept(department);
        return  ServerResponse.createBySuccess("添加成功",department);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_DELETE')")
    @myLog(value="删除部门")
    @DeleteMapping("/delDept/{id}")
    public ServerResponse delDept(@PathVariable Long id){
        departmentService.delDept(id);
        return ServerResponse.createBySuccessMessage("删除成功");
    }

    @PreAuthorize("hasAnyRole('ADMIN','DEPT_ALL','DEPT_EDIT')")
    @myLog(value="修改部门")
    @PostMapping("/updateDept")
    public ServerResponse updateDept(Department department){
        departmentService.updateDept(department);
        return ServerResponse.createBySuccess("修改成功",department);
    }


}
