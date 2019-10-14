package com.admin.demo.service;


import com.admin.demo.entity.EmailConfig;
import com.admin.demo.entity.EmailVo;
import org.springframework.scheduling.annotation.Async;

/**
 * @author Zheng Jie
 * @date 2018-12-26
 */
public interface EmailToolService {

    /**
     * 更新邮件配置
     * @param emailConfig
     * @param old
     * @return
     */
    void update(EmailConfig emailConfig, EmailConfig old);

    /**
     * 查询配置
     * @return
     */
    EmailConfig find();

    /**
     * 发送邮件
     * @param emailVo
     * @param emailConfig
     * @throws Exception
     */
    @Async
    void send(EmailVo emailVo, EmailConfig emailConfig) throws Exception;
}
