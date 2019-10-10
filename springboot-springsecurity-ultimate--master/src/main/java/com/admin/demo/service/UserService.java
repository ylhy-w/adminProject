package com.admin.demo.service;

import com.admin.common.exception.BadRequestException;
import com.admin.common.utils.*;
import com.admin.demo.entity.Attempts;
import com.admin.demo.entity.QueryVo;
import com.admin.demo.entity.Role;
import com.admin.demo.mapper.UserMapper;
import com.admin.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;

/**
 * @author: zzx
 * @date: 2018/10/15 16:54
 * @description: 用户认证、权限
 */
@Component
@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;


    @Override
    public  UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //通过username查询用户
        User user = userMapper.getUser(username);
        if(user == null){
            //仍需要细化处理
            throw new UsernameNotFoundException("该用户不存在");
        }
        return user;
    }


    //记录错误次数
    public void errorLogin(Long id) {
        Attempts attempt=new Attempts();
        attempt.setUser_id(id);
        Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
        attempt.setLast_time(date);
        userMapper.errorLogin(attempt);
    }

    //查询错误次数
    public int getAttempts(Long id) {
        return userMapper.getAttempts(id);
    }



    //重置密码错误次数
    public void reset(Long id) {
        userMapper.reset(id);
    }

    public int updatePassword(String password, String repassword) {
        //获取当前登录用户的密码
        String pass = SecurityUtils.getUserDetails().getPassword();
        //比对原始密码
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //判断加密后密码一致性
        boolean b=encoder.matches(password,pass);
        if (b){
            //如果一致，获取对象后修改密码
            //获取用户名
            User user = userMapper.getUser( SecurityUtils.getUsername());
            String hashPass = encoder.encode(repassword);

            user.setPassword(hashPass);
            //修改密码
            return   userMapper.updatePassword(user);
        }else {
            throw new BadRequestException("密码错误");
        }
    }

    public Object queryAll(QueryVo queryVo) {
        Map<String,Object> map = new HashMap<>();
if (queryVo.getDeptIds().size()==0){
    List<User> userList = userMapper.query(queryVo);
    Integer total =userMapper.count(queryVo);
 map.put("userList",userList);
 map.put("total",total);
    return map;
}
        List<User> userList = userMapper.queryAll(queryVo);
        Integer total =userMapper.countAll(queryVo);
        map.put("userList",userList);
        map.put("total",total);
        return map ;
    }


    public void addUser(User user) {
        if (userMapper.checkUsername(user.getUsername())>0){
            throw new BadRequestException("用户名已存在");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        //初始密码
        String hashPass =encoder.encode("123456");
        user.setPassword(hashPass);
        userMapper.addUser(user);
        userMapper.regAttempts(user.getId());
        //分配角色
        if (user.getRoles()!=null){
            List<Role> roles = user.getRoles();
            List<Long> rids = new ArrayList<Long>();
            for(Role role : roles){
                rids.add(role.getId());
            }
            userMapper.addUserRole(user.getId(),rids);
        }

    }

//修改用户
    public void updateUser(User user) {
        if (userMapper.check(user)>0){
            throw  new BadRequestException("用户名已存在");
        }
        if (user.getRoles()!=null) {
            userMapper.delUserRole(user.getId());
            List<Role> roles = user.getRoles();
            List<Long> rids = new ArrayList<Long>();
            for (Role role : roles) {
                rids.add(role.getId());
            }
            userMapper.addUserRole(user.getId(), rids);
        }
        userMapper.updateUser(user);
    }


//删除角色，需要根据需求再细化处理
    public void delUser(Long id) {
        User user = userMapper.findById(id);
        if (user.getRoles()!=null) {
            userMapper.delUserRole(id);
        }

        userMapper.delUser(id);
    }


    public User findById(Long id) {
        return userMapper.findById(id);
    }

    public User findByAccount(String account){
        return userMapper.findByAccount(account);
    }

}

