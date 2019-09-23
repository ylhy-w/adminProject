package com.admin.demo.service;

import com.admin.common.utils.ServerResponse;
import com.admin.demo.entity.User;

public interface EmailService {
    void emailActive(String email);

    void register(String username, String password, String email, String code);

    ServerResponse verifyMail(String sid, String account);

    void findPassowrd(String basePath, User user);

    void changeEmail(String password, String email, String code);
}
