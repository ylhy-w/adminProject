package com.admin.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class Picture {
    private Long id;

    private String filename;

    private String url;

    private String size;

    private String height;

    private String width;

    @JsonIgnore
    private String delete;

    private String username;

    private Timestamp createTime;
}
