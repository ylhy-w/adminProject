package com.admin.demo.entity;



import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class DictDetail implements Serializable {

    private Long id;

    private String label;

    private String value;

    private String sort;

    private Long dictId;

    private String name;

}