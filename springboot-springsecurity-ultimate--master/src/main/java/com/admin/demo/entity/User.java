package com.admin.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;



/**
 * @author: zzx
 * @date: 2018/10/15 16:58
 * @description: 定义user对象
 */


public class User implements UserDetails{

    private Long id;
    private String username;
    private String phone;
    private boolean enabled;
    private String password;
    private String email;
    private Long deptId;

    private Long posId;

    @JsonIgnore
    private List<Permission> permissions;
    private List<Department> departments;
    private List<Position> positions;
    private List<Role> roles;
    private String picture;
    private Timestamp create_time;
    private Timestamp last_password_reset_time;
    private boolean accountNonLocked;
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
            for (Permission permission : permissions) {
                //权限构建
                authorities.add(new SimpleGrantedAuthority(permission.getName()));
            }

        return authorities;
    }
    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }


    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }



    public Long getPosId() {
        return posId;
    }

    public void setPosId(Long posId) {
        this.posId = posId;
    }



    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    @JsonIgnore
    public Timestamp getLast_password_reset_time() {
        return last_password_reset_time;
    }

    public void setLast_password_reset_time(Timestamp last_password_reset_time) {
        this.last_password_reset_time = last_password_reset_time;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", enabled=" + enabled +
                ", email='" + email + '\'' +
                ", permission=" + permissions +
                ", picture='" + picture + '\'' +
                ", create_time=" + create_time +
                ", accountNonLocked=" + accountNonLocked +
                '}';
    }

    @Override	public int hashCode() {
        return username.hashCode();
    }
    @Override	public boolean equals(Object obj) {
        return this.toString().equals(obj.toString());
    }


}