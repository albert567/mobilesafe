package com.itheima.mobilesafe.domain;

/**
 * 黑名单号码的业务Bean
 * Created by zyp on 2016/7/3.
 */
public class BlackNumberInfo {
    private String id;
    private String phone;//黑名单号码
    private String mode;//模式

    public BlackNumberInfo(){}
    public BlackNumberInfo(String id, String phone, String mode) {
        this.id = id;
        this.phone = phone;
        this.mode = mode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
