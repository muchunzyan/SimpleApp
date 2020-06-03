package com.bifexey.simpleapp;

public class UserInfo {
    private String first_name;
    private String second_name;
    private String location;
    private String activity;

    public UserInfo(String first_name, String second_name, String location, String activity) {
        this.first_name = first_name;
        this.second_name = second_name;
        this.location = location;
        this.activity = activity;
    }

    public UserInfo() {

    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String location) {
        this.activity = activity;
    }
}
