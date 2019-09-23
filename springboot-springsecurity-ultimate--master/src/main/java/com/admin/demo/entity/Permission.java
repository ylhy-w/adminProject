package com.admin.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
public class Permission {
    private Long id;
    private String alias;
    private Date createTime;
    private String name;
    private Long pid;
    private List<Permission> children;

    @Override
    public String toString() {
        return "Permission{" +
                "id=" + id +
                ", alias='" + alias + '\'' +
                ", createTime=" + createTime +
                ", name='" + name + '\'' +
                ", pid=" + pid +
                ", children=" + children +
                '}';
    }
}
