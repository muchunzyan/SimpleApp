package com.bifexey.simpleapp;

public class ChatForm {
    String name;
    String phone_number;
    String activity;

    public ChatForm(String name, String phone_number, String activity) {
        this.name = name;
        this.phone_number = phone_number;
        this.activity = activity;
    }

    public ChatForm() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}
