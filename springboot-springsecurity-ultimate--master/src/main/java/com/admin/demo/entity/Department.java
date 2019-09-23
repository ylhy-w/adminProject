package com.admin.demo.entity;

import lombok.Getter;

import lombok.Setter;

import java.sql.Timestamp;
@Getter
@Setter
public class Department  {
    private Long id;

    private String name;

    private Boolean enabled;

    private Long pid;


    //private Set<Role> roles;

    private Timestamp createTime;

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", enabled=" + enabled +
                ", pid=" + pid +
                ", createTime=" + createTime +
                '}';
    }
}
