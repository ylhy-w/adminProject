package com.admin.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 * 返回token
 */
@Getter
@Setter
@AllArgsConstructor
public class AuthenticationInfo implements Serializable {
    private  String token;
    private  User user;

}
