package com.admin.demo.controller;


import com.admin.common.config.DataScope;
import com.admin.common.utils.ServerResponse;
import com.admin.demo.entity.Position;
import com.admin.demo.entity.QueryVo;
import com.admin.demo.service.DepartmentService;
import com.admin.demo.service.PositionService;
import com.admin.demo.service.RoleService;
import com.admin.log.myLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("pos")
public class PositionController {

    @Autowired
    private DataScope dataScope;

    @Autowired
    PositionService positionService;

    @Autowired
    DepartmentService departmentService;

    //岗位下拉列表
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT','USER_CREATE','USER_SELECT','USER_DELETE')")
    @GetMapping(value = "/all")
    public ResponseEntity getAll(){
        List<Position> positions = positionService.getAll();
        return ResponseEntity.ok(positions);
    }

    //id查询
    @PreAuthorize("hasAnyRole('ADMIN','USERJOB_ALL','USERJOB_EDIT','USERJOB_CREATE','USERJOB_SELECT','USERJOB_DELETE')")
    @GetMapping(value = "/{id}")
    public ResponseEntity findById(@PathVariable Long id){
        List<Position> positions = positionService.findById(id);
        return ResponseEntity.ok(positions);
    }

    @myLog("添加岗位")
    @PreAuthorize("hasAnyRole('ADMIN','USERJOB_ALL','USERJOB_CREATE')")
    @PostMapping(value = "/addPos")
    public ServerResponse addPos(Position position){
        positionService.addPos(position);
        return ServerResponse.createBySuccess("添加成功",position);
    }

    @myLog("删除岗位")
    @PreAuthorize("hasAnyRole('ADMIN','USERJOB_ALL','USERJOB_DELETE')")
    @DeleteMapping(value = "/delPos/{id}")
    public ServerResponse delPos(@PathVariable Long id){
        positionService.delPos(id);
        return ServerResponse.createBySuccess("删除成功");
    }


    @myLog("修改岗位")
    @PreAuthorize("hasAnyRole('ADMIN','USERJOB_ALL','USERJOB_EDIT')")
    @PostMapping(value = "/updatePos")
    public ServerResponse updatePos(Position position){
        positionService.updatePos(position);
        return ServerResponse.createBySuccess("修改成功",position);
    }

    @myLog("查询岗位")
    @PreAuthorize("hasAnyRole('ADMIN','USERJOB_ALL','USERJOB_SELECT')")
    @GetMapping(value = "/getPos")
    public ResponseEntity getPos(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(defaultValue = "") String name,
                                     String state,
                                   boolean enabled
    ){
        Set<Long> deptSet = new HashSet<>();
        Set<Long> result = new HashSet<>();
        QueryVo queryVo=new QueryVo();

        int start= (page-1)*size;
        queryVo.setPage(start);
        queryVo.setSize(size);
        queryVo.setName(name);
        queryVo.setEnabled(enabled);
        queryVo.setState(state);

        if (!ObjectUtils.isEmpty(queryVo.getDeptId())) {
            deptSet.add(queryVo.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(departmentService.findByPid(queryVo.getDeptId())));
//            System.out.println("getDeptId为空  "+deptSet);
        }

        // 数据权限
        Set<Long> deptIds = dataScope.getDeptIds();
        System.out.println("数据权限"+deptIds);
        // 查询条件不为空并且数据权限不为空则取交集

        if (!CollectionUtils.isEmpty(deptIds)&& !CollectionUtils.isEmpty(deptSet) ){
           //   System.out.println("取交集");
            // 取交集
            result.addAll(deptSet);
            result.retainAll(deptIds);
            // 若无交集，则代表无数据权限
            queryVo.setDeptIds(result);
            if(result.size() == 0){
                return new ResponseEntity(HttpStatus.OK);
            } else return new ResponseEntity(positionService.queryAll(queryVo),HttpStatus.OK);
            // 否则取并集
        } else {
        //System.out.println("取并集");
            result.addAll(deptSet);
            result.addAll(deptIds);
            //       System.out.println(result.toString());
            queryVo.setDeptIds(result);
            return new ResponseEntity(positionService.queryAll(queryVo),HttpStatus.OK);
        }
    }
}
