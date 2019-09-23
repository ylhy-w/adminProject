package com.admin.demo.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Attempts {
    private Long id;
    private Long user_id;
    private int attempts;
    private Date last_time;


}
