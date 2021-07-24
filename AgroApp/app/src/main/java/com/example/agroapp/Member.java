package com.example.agroapp;

import android.widget.ScrollView;

public class Member {

    private String name;
    private String email;
    private String mobile;
    private String address;
    private String usertype;

    public Member(String name, String email, String mobile, String address, String usertype){
        this.name=name;
        this.email=email;
        this.mobile=mobile;
        this.address=address;
        this.usertype=usertype;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUsertype() {
        return usertype;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAddress() {
        return address;
    }
}
