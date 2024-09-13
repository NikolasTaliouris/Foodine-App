package com.capstone.foodineapp.Models;

import java.io.Serializable;

public class User implements Serializable {

    /* Object class for the User , We created this object to help us store and display what we want for our User. It's necessary for the
     * real time database*/
    private String name;
    private String password;

    private String phone;



    public User() {
    }

    public User(String name, String password, String phone) {
        this.name = name;
        this.password = password;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
