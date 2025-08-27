package com.randomuser.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Registered {
    private String date;
    private int age;

    // Getters and setters
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
}