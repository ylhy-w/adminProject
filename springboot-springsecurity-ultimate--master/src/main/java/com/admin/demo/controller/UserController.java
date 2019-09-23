package com.admin.demo.controller;

import com.admin.common.config.DataScope;
import com.admin.common.exception.BadRequestException;
import com.admin.common.utils.JwtTokenUtil;
import com.admin.common.utils.SecurityUtils;
import com.admin.common.utils.ServerResponse;
import com.admin.demo.entity.AuthenticationInfo;
import com.admin.demo.entity.QueryVo;

import com.admin.demo.entity.Role;
import com.admin.demo.entity.User;
import com.admin.demo.service.DepartmentService;
import com.admin.demo.service.RoleService;
import com.admin.demo.service.UserService;
import com.admin.log.myLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author: zzx
 * @date: 2018/10/25 16:48
 * @description: demo
 */
@RestController
@RequestMapping("auth")
public class UserController {

    @Autowired
    UserService selfUserDetailsService;

    @Autowired
    private DataScope dataScope;

    @Autowired
    RoleService roleService;

    @Autowired
    private DepartmentService departmentService;


    @Value("${jwt.header}")
    private String tokenHeader;

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



    @myLog(value = "修改密码")
    @PostMapping(value = "/updatePassword")
    public ServerResponse updatePassword(String password,String repassword){
        if (selfUserDetailsService.updatePassword(password,repassword)==1){
            return ServerResponse.createBySuccessMessage("修改成功");
        }
        throw new BadRequestException("修改失败");
    }



    @myLog(value = "添加用户")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_CREATE')")
    @PostMapping(value = "/addUser")
    public ServerResponse addUser(@RequestBody User user){
        checkLevel(user);
        selfUserDetailsService.addUser(user);
        return ServerResponse.createBySuccess("添加成功",user);
    }

    @myLog(value = "修改用户")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT')")
    @PostMapping(value = "/updateUser")
    public ServerResponse updateUser(@RequestBody User user){
        checkLevel(user);
        selfUserDetailsService.updateUser(user);
        return ServerResponse.createBySuccess("修改成功",user);
    }


    @myLog(value = "删除用户")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_DELETE')")
    @DeleteMapping(value = "/delUser/{id}")
    public ServerResponse delUser(@PathVariable Long id){
        if (id.equals(SecurityUtils.getUserId())){
            throw new BadRequestException("不能删除自己");
        }

        User user = new User();
        user.setId(id);
        checkLevel(user);
        selfUserDetailsService.delUser(id);
        return ServerResponse.createBySuccessMessage("删除成功");
    }


    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_EDIT','USER_CREATE','USER_SELECT')")
    @GetMapping(value = "/{id}")
    public ResponseEntity updateUser(@PathVariable Long id){
        User user = selfUserDetailsService.findById(id);
        return ResponseEntity.ok(user);
    }

    @myLog("查询用户")
    @PreAuthorize("hasAnyRole('ADMIN','USER_ALL','USER_SELECT')")
    @GetMapping(value = "/users")
    public ResponseEntity getUsers(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(defaultValue = "") String name,
                                   @RequestParam(defaultValue = "") String deptName,
                                   @RequestParam(defaultValue = "true") boolean enabled,
                                   Long deptId
                                  ){
        Set<Long> deptSet = new HashSet<>();
        Set<Long> result = new HashSet<>();
        QueryVo queryVo=new QueryVo();

        int start= (page-1)*size;
        queryVo.setPage(start);
        queryVo.setSize(size);
        queryVo.setDeptName(deptName);
        queryVo.setName(name);
        queryVo.setEnabled(enabled);
        queryVo.setDeptId(deptId);


        if (!ObjectUtils.isEmpty(queryVo.getDeptId())) {
            deptSet.add(queryVo.getDeptId());
            deptSet.addAll(dataScope.getDeptChildren(departmentService.findByPid(queryVo.getDeptId())));
//            System.out.println("getDeptId为空  "+deptSet);
        }

        // 数据权限
        Set<Long> deptIds = dataScope.getDeptIds();
        System.out.println("数据权限"+deptIds);
        // 查询条件不为空并且数据权限不为空则取交集
        if (!CollectionUtils.isEmpty(deptIds) && !CollectionUtils.isEmpty(deptSet)){
       //     System.out.println("取交集");
            // 取交集
            result.addAll(deptSet);
            result.retainAll(deptIds);
            // 若无交集，则代表无数据权限
            queryVo.setDeptIds(result);
            if(result.size() == 0){
                return new ResponseEntity(HttpStatus.OK);
            } else return new ResponseEntity(selfUserDetailsService.queryAll(queryVo),HttpStatus.OK);
            // 否则取并集
        } else {
       //     System.out.println("取并集");
            result.addAll(deptSet);
            result.addAll(deptIds);
     //       System.out.println(result.toString());
            queryVo.setDeptIds(result);
            return new ResponseEntity(selfUserDetailsService.queryAll(queryVo),HttpStatus.OK);
        }
    }

    /**
     * 如果当前用户的角色级别低于或同级创建用户的角色级别，则抛出权限不足的错误
     */
    private void checkLevel(User user) {
        //当前用户角色level
        Integer  optLevel = 0;
        Integer currentLevel = roleService.findLevel(SecurityUtils.getUserId());
        //要改动的用户角色level
        if (user.getRoles()==null){
            optLevel= roleService.findLevel(user.getId());
        }else{
          optLevel = roleService.findByRoles(user.getRoles());
        }

        if (currentLevel >= optLevel) {
            throw new BadRequestException("角色权限不足");
        }
    }


}
