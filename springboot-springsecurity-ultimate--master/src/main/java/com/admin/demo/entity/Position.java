package com.admin.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Position {

    private Long id;

    private String name;

    private String superDeptName;

    private Long sort;

    private Boolean enabled;

    private Long deptId;

    private Department dept;

    private Timestamp createTime;

    @Override
    public String toString() {
        return "Position{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", superDeptName='" + superDeptName + '\'' +
                ", sort=" + sort +
                ", enabled=" + enabled +
                ", deptId=" + deptId +
                ", dept=" + dept +
                ", createTime=" + createTime +
                '}';
    }
}
