package com.admin.demo.mapper;

import com.admin.demo.entity.MailRetrieve;

public interface MailRetrieveMapper {
    MailRetrieve findByAccount(String account);

    void delete(String account);

    void save(MailRetrieve mailRetrieve);

    void update(String account);
}
