package com.randomuser.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Id {
    private String name;
    private String value;

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}