package com.admin.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
public class QueryVo {
    private Long deptId;
    private String deptName;
    private Boolean enabled;
    private String name;
    private Integer page;
    private Integer size;
    private Set<Long> deptIds;
    private String keywords;

    //辅助字段 开启enabled属性
    private String state;

    @Override
    public String toString() {
        return "QueryVo{" +
                "deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", enabled=" + enabled +
                ", name='" + name + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", deptIds=" + deptIds +
                ", keywords='" + keywords + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
