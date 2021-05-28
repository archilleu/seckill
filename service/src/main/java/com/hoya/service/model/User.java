package com.hoya.service.model;

import lombok.Data;

@Data
public class User {

    private int id;

    private String name;

    private int age;

    private String address;

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", age=" + age + "]";
    }

}
