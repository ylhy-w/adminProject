package com.admin.demo.controller;

import com.admin.common.utils.RequestHolder;
import com.admin.demo.service.VisitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Zheng Jie
 * @date 2018-12-13
 */
@RequestMapping("api/visits")
@RestController
public class VisitsController {

    @Autowired
    private VisitsService visitsService;

    @PostMapping()
    public ResponseEntity create(){
        visitsService.count(RequestHolder.getHttpServletRequest());
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity get(){
        return new ResponseEntity(visitsService.get(), HttpStatus.OK);
    }

    @GetMapping(value = "/chartData")
    public ResponseEntity getChartData(){
        return new ResponseEntity(visitsService.getChartData(), HttpStatus.OK);
    }
}
