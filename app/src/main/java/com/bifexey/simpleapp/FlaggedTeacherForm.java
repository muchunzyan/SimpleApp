package com.bifexey.simpleapp;

public class FlaggedTeacherForm {
    String teacher_phone_number;
    String teacher_name;

    public FlaggedTeacherForm(String teacher_phone_number, String teacher_name) {
        this.teacher_phone_number = teacher_phone_number;
        this.teacher_name = teacher_name;
    }

    public FlaggedTeacherForm() {
    }

    public String getTeacher_phone_number() {
        return teacher_phone_number;
    }

    public void setTeacher_phone_number(String teacher_phone_number) {
        this.teacher_phone_number = teacher_phone_number;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }
}
