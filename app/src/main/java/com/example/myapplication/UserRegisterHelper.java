package com.example.myapplication;

import java.util.ArrayList;

public class UserRegisterHelper {
    String age,phoneno,email,password,name,ts;
    ArrayList<Integer> al;
    public UserRegisterHelper() {
    }

    public UserRegisterHelper(String name, String phoneno, String email, String password, String age) {
        this.name = name;
        this.phoneno = phoneno;
        this.email = email;
        this.age=age;
        this.password = password;
    }
    public UserRegisterHelper(String name, String phoneno, String email, String password, String age,String ts) {
        this.name = name;
        this.phoneno = phoneno;
        this.email = email;
        this.age=age;
        this.password = password;
        this.ts=ts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTs(){return ts;}

    public void setTs(String ts){this.ts=ts;}

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
