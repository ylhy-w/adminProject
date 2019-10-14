package com.admin.demo.entity;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 发送邮件时，接收参数的类
 * @author 郑杰
 * @date 2018/09/28 12:02:14
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EmailVo {

    /**
     * 收件人，支持多个收件人，用逗号分隔
     */
    @NotEmpty
    private List<String> tos;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
