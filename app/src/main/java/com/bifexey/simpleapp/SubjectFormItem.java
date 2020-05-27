package com.bifexey.simpleapp;

public class SubjectFormItem {
    private String name;
    private String subject;
    private String special_subject;
    private String price;
    private String comment;
    private String key;
    private String phone_number;

    public SubjectFormItem(String name, String subject, String special_subject, String price, String comment, String key, String phone_number) {
        this.name = name;
        this.subject = subject;
        this.special_subject = special_subject;
        this.price = price;
        this.comment = comment;
        this.key = key;
        this.phone_number = phone_number;
    }

    public SubjectFormItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSpecial_subject() {
        return special_subject;
    }

    public void setSpecial_subject(String special_subject) {
        this.special_subject = special_subject;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
