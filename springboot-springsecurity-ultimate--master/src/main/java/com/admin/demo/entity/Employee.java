package com.admin.demo.entity;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Employee {
    private Long id;
    private String sex;
    private String name;
    private String email;
    private String phone;
    private String address;
    private Long deptId;
    private String deptName;
    private Long posId;
    private String posName;
    private String beginDate;

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", sex='" + sex + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", posId=" + posId +
                ", posName='" + posName + '\'' +
                ", beginDate='" + beginDate + '\'' +
                '}';
    }
}
