package com.itheima.mobilesafe.domain;

/**
 * 联系人的业务bean
 * Created by zyp on 2016/6/25.
 */
public class ContactInfo {
    private String name;
    private String phone;
    private String email;
    private String qq;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
