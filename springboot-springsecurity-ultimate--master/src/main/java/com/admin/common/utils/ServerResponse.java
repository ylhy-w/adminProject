package com.admin.common.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

@JsonSerialize(include =  JsonSerialize.Inclusion.NON_NULL)
//保证序列化json的时候,如果是null的对象,key也会消失
public class ServerResponse<T> implements Serializable{
    private int status;
    private String message;
    private T data;
    private ServerResponse(int status){
        this.status = status;
    }
    private ServerResponse(int status,T data){
        this.status = status;
        this.data = data;
    }
    private ServerResponse(int status,String message,T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }     private ServerResponse(int status,String message){
        this.status = status;
        this.message = message;
    }
    @JsonIgnore
    //响应是否正确的判断,通过注解在序列化时，isSuccess方法就不会在序列化里
//使之不在json序列化结果当中
public boolean isSuccess(){
        //状态为000000（成功）即TRUE，否则FALSE
        return this.status == ResponseCode.SUCCESS.getCode();
    }
    public int getStatus(){
        return status;
    }

    public T getData(){
        return data;
    }

    public String getMessage(){
        return message;
    }

    //声明这个类，创建这个类，通过相应成功后，通过成功标志来构件这个类，最终返回一个status

    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode());
    }

//通过文本显示成功，创建这个类，作用是供前端提示使用

    public static <T> ServerResponse<T> createBySuccessMessage(String message){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),message);
    }

//相应成功然后给前台相应数据

    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),data);
    }

//相应成功然后给前台相应数据和文本消息
    public static <T> ServerResponse<T> createBySuccess(String message,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),message,data);
    }


//响应失败时，直接把错误返回
    public static <T> ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),ResponseCode.ERROR.getDesc());
    }

//直接返回错误信息
    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),errorMessage);
    }


//服务端响应，需要登录/参数错误
    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }

    @Override
    public String toString() {
        return "ServerResponse{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
