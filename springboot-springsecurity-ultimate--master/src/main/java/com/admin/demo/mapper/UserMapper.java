package com.admin.demo.mapper;

import com.admin.demo.entity.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author: zzx
 * @date: 2018/10/18 14:59
 * @description: 用户dao层
 */
@Component
public interface UserMapper {

    //通过username查询用户
    User getUser(@Param("username") String username);

    List<Permission> getPermissionById(Long id);

    List<Role> getRolesById(Long id);

    int register(User user);

    void regAttempts(Long user_id);

    int checkUsername(String username);

    void errorLogin(Attempts attempt);

    int getAttempts(Long id);

    void updateUser(User user);

    void reset(Long id);

    int updatePassword(User user);

    List<User> queryAll(QueryVo queryVo);

    List<User> query(QueryVo queryVo);

    User findByAccount(String account);

    void updateEmail(User user);

    void addUser(User user);


    void addUserRole(Long id, List<Long> rids);

    void delUser(Long id);

    void delUserRole(Long id);

    Integer check(User user);

    Integer count(QueryVo queryVo);

    Integer countAll(QueryVo queryVo);

    User findById(Long id);
}
