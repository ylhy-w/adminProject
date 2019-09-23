package com.admin.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class QueryVo {
    private Long deptId;
    private String deptName;
    private boolean enabled;
    private String name;
    private Integer page;
    private Integer size;
    private Set<Long> deptIds;
    private String keywords;
}