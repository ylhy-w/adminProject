package com.admin.demo.controller;

import com.admin.common.utils.ServerResponse;
import com.admin.demo.entity.Dict;
import com.admin.demo.service.DictService;
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
@RequestMapping("api/dict")
public class DictController {

    @Autowired
    private DictService dictService;


    @myLog("查询字典")
    @GetMapping(value = "/getDict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_SELECT')")
    public ResponseEntity getDict(@RequestParam(defaultValue = "1") Integer page,
                                  @RequestParam(defaultValue = "10") Integer size,
                                  @RequestParam(defaultValue = "") String keywords){
        int start = (page-1)*size;
        return new ResponseEntity(dictService.getDict(start,size,keywords), HttpStatus.OK);
    }

    @myLog("新增字典")
    @PostMapping(value = "/addDict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_CREATE')")
    public ServerResponse addDict(Dict dict){
        dictService.addDict(dict);
        return ServerResponse.createBySuccess("添加成功",dict);
    }

    @myLog("修改字典")
    @PostMapping(value = "/updateDict")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_EDIT')")
    public ServerResponse updateDict(Dict dict){
        dictService.updateDict(dict);
        return ServerResponse.createBySuccess("修改成功",dict);
    }

    @myLog("删除字典")
    @DeleteMapping(value = "/delDict/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DICT_ALL','DICT_EDIT')")
    public ServerResponse delDict(@PathVariable Long id){
        dictService.delDict(id);
        return ServerResponse.createBySuccessMessage("删除成功");
    }
}