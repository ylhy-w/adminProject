package com.admin.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@Setter
@Getter
public class Role {
    private Long id;
    private String name;
    private Timestamp createTime;
    private String remark;
    private String dataScope;
    private Integer level;
    private List<Permission> permissions;
    private List<Menu> menus;
    private List<Department> depts;



    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", createTime=" + createTime +
                ", remark='" + remark + '\'' +
                ", dataScope='" + dataScope + '\'' +
                ", level=" + level +
                ", permissions=" + permissions +
                ", menus=" + menus +
                ", depts=" + depts +
                '}';
    }
}
