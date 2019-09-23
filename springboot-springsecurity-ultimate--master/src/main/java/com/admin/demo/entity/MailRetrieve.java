package com.admin.demo.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.Serializable;

@Getter
@Setter
public class MailRetrieve implements Serializable {
    private long id;
    private String account;
    private String sid;
    private long outTime;
    public MailRetrieve() {

    }
    public MailRetrieve(String account, String sid, long outTime) {
        this.account = account;
        this.sid = sid;
        this.outTime = outTime;    }


}
