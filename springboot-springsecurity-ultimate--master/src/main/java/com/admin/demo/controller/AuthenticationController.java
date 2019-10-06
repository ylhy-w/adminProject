package com.admin.demo.controller;

import com.admin.common.exception.BadRequestException;
import com.admin.common.utils.JwtTokenUtil;
import com.admin.common.utils.SecurityUtils;
import com.admin.demo.entity.AuthenticationInfo;
import com.admin.demo.entity.User;
import com.admin.demo.service.UserService;
import com.admin.log.myLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    UserService selfUserDetailsService;


    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @myLog(value = "用户登录")
    @PostMapping(value = "${jwt.auth.path}")
    public ResponseEntity login(User authorizationUser){


        User jwtUser = (User) selfUserDetailsService.loadUserByUsername(authorizationUser.getUsername());

        if(!jwtUser.isAccountNonLocked()){
            throw new AccountExpiredException("账号已锁定，请联系管理员");
        }

        if(!jwtUser.isEnabled()){
            throw new AccountExpiredException("账号已停用，请联系管理员");
        }

        String pass=authorizationUser.getPassword();
        //比对原始密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //判断加密后密码一致性
        boolean b=encoder.matches(pass,jwtUser.getPassword());
        if(!b){
            selfUserDetailsService.errorLogin(jwtUser.getId());
            int attempts = selfUserDetailsService.getAttempts(jwtUser.getId());
            int num=5-attempts;
            if(attempts>4){
                jwtUser.setAccountNonLocked(false);
                selfUserDetailsService.updateUser(jwtUser);
                //   logger.info("密码错误次数过多  "+username+"账号锁定");
            }
            throw new BadRequestException("密码错误,"+"剩余"+num+"次机会");
        }else {
            selfUserDetailsService.reset(jwtUser.getId());
        }


        // 生成令牌
        final String token = jwtTokenUtil.generateToken(jwtUser);
        // 返回 token
        return ResponseEntity.ok(new AuthenticationInfo(token,jwtUser));
    }


    @myLog(value = "查询个人信息")
    @GetMapping(value = "${jwt.auth.account}")
    public ResponseEntity getUserInfo(){
        User jwtUser = (User)selfUserDetailsService.loadUserByUsername(SecurityUtils.getUsername());
        return ResponseEntity.ok(jwtUser);
    }

}
