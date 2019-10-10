package com.admin.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;


@Getter
@Setter
public class Menu implements Serializable {

    private Long id;


    private String name;

    private Long sort;

    private String path;


    private String component;

    private String icon;
    /**
     * 上级菜单ID
     */
    private Long pid;
    /**
     * 是否为外链 true/false
     */
    private Boolean iFrame;

    @JsonIgnore
    private Timestamp createTime;

    private boolean enabled;



    private List<Menu> children;

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sort=" + sort +
                ", path='" + path + '\'' +
                ", component='" + component + '\'' +
                ", icon='" + icon + '\'' +
                ", pid=" + pid +
                ", iFrame=" + iFrame +
                ", createTime=" + createTime +
                ", enabled=" + enabled +
                ", children=" + children +
                '}';
    }
}
