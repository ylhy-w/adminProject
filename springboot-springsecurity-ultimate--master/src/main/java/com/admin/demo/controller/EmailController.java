package com.admin.demo.controller;


import com.admin.common.exception.BadRequestException;
import com.admin.common.utils.ServerResponse;
import com.admin.demo.entity.User;
import com.admin.demo.service.EmailService;
import com.admin.demo.service.UserService;
import com.admin.log.myLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EmailController {
    @Autowired
    EmailService emailService;
    @Autowired
    UserService userService;


    //邮件发送注册激活码
    @PostMapping(value = "/emailActive")
    public ServerResponse emailActive(String email){
        emailService.emailActive(email);
        return ServerResponse.createBySuccessMessage("发送成功,请注意查收") ;
    }

    @myLog(value = "修改邮箱")
    @PostMapping(value = "/changeEmail")
    public ServerResponse changeEmail(String password,String email,String code){
        emailService.changeEmail(password,email,code);
        return ServerResponse.createBySuccessMessage("修改成功") ;
    }

    @myLog(value = "用户注册")
    @PostMapping(value = "/register")
    public ServerResponse register(String username, String password,String email,String code){
        emailService.register(username,password,email,code);
        return ServerResponse.createBySuccessMessage("注册成功") ;
    }


    //验证重置密码url
    @GetMapping(value = "/reset_password")
    public ServerResponse verifyMail(String account,String sid){
        return emailService.verifyMail(sid,account);
    }

    @myLog(value = "忘记密码")
    @PostMapping(value = "/forgetPwd")
    public ServerResponse forgetPwd(HttpServletRequest request, String account){
      User user=userService.findByAccount(account);
        if (user == null) {
            throw new BadRequestException("该用户不存在");
        }
        if (user.getEmail()==null){
            throw new BadRequestException("该用户尚未绑定邮箱");
        }
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        emailService.findPassowrd(basePath,user);
        return ServerResponse.createBySuccessMessage("重置密码邮件已成功发送至账号绑定的邮箱，请注意查收");

    }
}
