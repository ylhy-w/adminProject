package com.admin.demo.controller;

import com.admin.demo.entity.Employee;
import com.admin.demo.service.EmpService;
import com.admin.log.myLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/emp")

public class EmpController {


    @Autowired
    EmpService empService;

 // @PreAuthorize("hasAnyAuthority('USER_SELECT','ADMIN')")
    @myLog(value = "查询员工信息")
    @GetMapping("/get")

                public ResponseEntity getEmps(@RequestParam(defaultValue = "1") Integer page,
    @RequestParam(defaultValue = "10") Integer size,
    @RequestParam(defaultValue = "") String keywords,
    Long posId,
    Long deptId,
    String beginDate) {
        Map<String, Object> map = new HashMap<>();
        List<Employee> employeeByPage = empService.getEmps(page, size,
                keywords, posId, deptId, beginDate);
        Long count = empService.getEmpsCount(keywords, posId, deptId, beginDate);
        map.put("emps", employeeByPage);
        map.put("count", count);
        return ResponseEntity.ok(map);
                }

                @GetMapping("/workId")
                        public ResponseEntity getMaxWorkId(){
            Long max=empService.getMaxWorkId();
            return ResponseEntity.ok(max);
                }


}
