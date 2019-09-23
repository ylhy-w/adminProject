package com.admin.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Position {

    private Long id;

    private String name;

    private Long sort;

    private Boolean enabled;

    private Long deptId;


    private Timestamp createTime;
}
