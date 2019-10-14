package com.admin.demo.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;


@Getter
@Setter
public class Dict implements Serializable {

    private Long id;

    private String name;

    private String remark;

}