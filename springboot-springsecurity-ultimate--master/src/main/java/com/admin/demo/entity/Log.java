package com.admin.demo.entity;

import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author Zheng Jie
 * @date 2018-11-24
 */

@Getter
@Setter
@AllArgsConstructor
public class Log  implements Serializable {
    private Long id;

    private String username;

    private String description;

    private String method;

    private String params;

    private String logType;

    private String requestIp;

    private String address;

    private Long time;

    private byte[] exceptionDetail;

    private Timestamp createTime;

    public Log(String logType, Long time) {
        this.logType = logType;
        this.time = time;
    }
    //必须有无参构造方法
    public Log() {
    }
}
