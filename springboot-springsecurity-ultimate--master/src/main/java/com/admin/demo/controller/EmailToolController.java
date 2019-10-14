package com.admin.demo.controller;

import com.admin.common.utils.ServerResponse;
import com.admin.demo.entity.EmailConfig;
import com.admin.demo.entity.EmailVo;
import com.admin.demo.service.EmailToolService;
import com.admin.log.myLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 发送邮件
 * @author 郑杰
 * @date 2018/09/28 6:55:53
 */
@Slf4j
@RestController
@RequestMapping("api/emailTool")
public class EmailToolController {

    @Autowired
    private EmailToolService emailService;

    @GetMapping(value = "/get")
    public ResponseEntity get(){
        return new ResponseEntity(emailService.find(), HttpStatus.OK);
    }

    @myLog("配置邮件")
    @PostMapping(value = "/configuration")
    public ServerResponse emailConfig(@Validated @RequestBody EmailConfig emailConfig){
        emailService.update(emailConfig,emailService.find());
        return ServerResponse.createBySuccessMessage("配置成功");
    }

    @myLog("发送邮件")
    @PostMapping(value = "/send")
    public ServerResponse send(@Validated @RequestBody EmailVo emailVo) throws Exception {
        log.warn("REST request to send Email : {}" +emailVo);
        emailService.send(emailVo,emailService.find());
        return ServerResponse.createBySuccessMessage("发送成功");
    }
}
