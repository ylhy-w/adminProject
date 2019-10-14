package com.admin.demo.entity;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 邮件配置类，数据存覆盖式存入数据存
 * @author Zheng Jie
 * @date 2018-12-26
 */

@Getter
@Setter
public class EmailConfig implements Serializable {

    private Long id;

    /**
     *邮件服务器SMTP地址
     */
    @NotBlank
    private String host;

    /**
     * 邮件服务器SMTP端口
     */
    @NotBlank
    private String port;

    /**
     * 发件者用户名，默认为发件人邮箱前缀
     */
    @NotBlank
    private String user;

    @NotBlank
    private String pass;

    /**
     * 发送人邮箱
     */
    @NotBlank
    private String fromUser;
}
