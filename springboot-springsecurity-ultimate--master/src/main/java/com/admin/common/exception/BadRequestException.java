package com.admin.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/**
 * @author Zheng Jie
 * @date 2018-11-23
 * 统一异常处理
 */
@Getter
public class BadRequestException extends RuntimeException{

    private Integer status = BAD_REQUEST.value();

    public BadRequestException(String msg){
        super(msg);
    }

    public BadRequestException(HttpStatus status, String msg){
        super(msg);
        this.status = status.value();
    }

    //减少异常堆栈信息打印
//    @Override
//    public Throwable fillInStackTrace() {
//        return this;
//    }

//    @Override
//    public String toString() {
//        //  return MessageFormat.format("{0}[{1}]",this.getStatus(),this.getMessage());
//        return MessageFormat.format("{0}",this.getMessage());
//    }
}
