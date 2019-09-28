package com.admin.demo.entity;

import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;

/**
 * pv 与 ip 统计
 *
 * @author Zheng Jie
 * @date 2018-12-13
 */

@Getter
@Setter
public class Visits {


    private Long id;

    private String date;

    private Long pvCounts;

    private Long ipCounts;

    private Timestamp createTime;

    private String weekDay;
}
