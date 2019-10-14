package com.admin.demo.service.impl;

import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;
import com.admin.common.exception.BadRequestException;
import com.admin.common.utils.EncryptUtils;
import com.admin.demo.entity.EmailConfig;
import com.admin.demo.entity.EmailVo;
import com.admin.demo.mapper.EmailToolMapper;
import com.admin.demo.service.EmailToolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author Zheng Jie
 * @date 2018-12-26
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class EmailToolServiceImpl implements EmailToolService {

    @Autowired
    private EmailToolMapper emailToolMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(EmailConfig emailConfig, EmailConfig old) {
        try {
            if(!emailConfig.getPass().equals(old.getPass())){
                // 对称加密
                emailConfig.setPass(EncryptUtils.desEncrypt(emailConfig.getPass()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        emailConfig.setId(old.getId());
        emailToolMapper.save(emailConfig);
    }

    @Override
    public EmailConfig find() {
            return emailToolMapper.findById(1L);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send(EmailVo emailVo, EmailConfig emailConfig){
        if(emailConfig == null){
            throw new BadRequestException("请先配置，再操作");
        }
        /**
         * 封装
         */
        MailAccount account = new MailAccount();
        account.setHost(emailConfig.getHost());
        account.setPort(Integer.parseInt(emailConfig.getPort()));
        account.setAuth(true);
        try {
            // 对称解密
            account.setPass(EncryptUtils.desDecrypt(emailConfig.getPass()));
        } catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
        //发送者邮箱
        account.setFrom(emailConfig.getUser()+"<"+emailConfig.getFromUser()+">");
        //ssl方式发送
        account.setSslEnable(true);
        String content = emailVo.getContent();
        /**
         * 发送
         */
        try {
            Mail.create(account)
                    .setTos(emailVo.getTos().toArray(new String[emailVo.getTos().size()]))
                    .setTitle(emailVo.getSubject())
                    .setContent(content)
                    .setHtml(true)
                    //关闭session
                    .setUseGlobalSession(false)
                    .send();
        }catch (Exception e){
            throw new BadRequestException(e.getMessage());
        }
    }
}
