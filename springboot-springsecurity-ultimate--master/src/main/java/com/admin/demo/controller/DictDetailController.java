package com.admin.demo.controller;

import com.admin.common.utils.ServerResponse;
import com.admin.demo.entity.DictDetail;
import com.admin.demo.service.DictDetailService;
import com.admin.log.myLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
* @author Zheng Jie
* @date 2019-04-10
*/
@RestController
@RequestMapping("api/dictDetail")
public class DictDetailController {

    @Autowired
    private DictDetailService dictDetailService;



    @myLog("查询字典详情")
    @GetMapping(value = "/getDetail")
    public ResponseEntity getDetails(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         Long dict_id){
        int start = (page-1)*size;
        return new ResponseEntity(dictDetailService.getDictDetails(start,size,dict_id), HttpStatus.OK);
    }

    @myLog("查询字典详情")
    @GetMapping(value = "/map")
    public ResponseEntity getDetailsMap(@RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10") Integer size,
                                        @RequestParam(defaultValue = "") String keywords,
                                        Long dict_id){
        int start = (page-1)*size;
        return new ResponseEntity(dictDetailService.getDetailsMap(start,size,dict_id,keywords), HttpStatus.OK);
    }

    @myLog("删除字典详情")
    @DeleteMapping(value = "/delDictDetail/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_DELETE')")
    public ServerResponse delDictDetail(@PathVariable Long id){
        dictDetailService.delDictDetail(id);
        return ServerResponse.createBySuccessMessage("删除成功");
    }

    @myLog("新增字典详情")
    @PostMapping(value = "/addDetail")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_CREATE')")
    public ServerResponse addDetail(DictDetail dictDetail){
        dictDetailService.addDetail(dictDetail);
        return ServerResponse.createBySuccess("添加成功",dictDetail);
    }

    @myLog("修改字典详情")
    @PostMapping(value = "/updateDetail")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_EDIT')")
    public ServerResponse updateDetail(DictDetail dictDetail){
        dictDetailService.updateDetail(dictDetail);
        return ServerResponse.createBySuccess("修改成功",dictDetail);
    }

}