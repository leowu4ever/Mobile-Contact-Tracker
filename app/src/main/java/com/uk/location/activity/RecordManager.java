package com.uk.location.activity;

public class RecordManager {
    private String gender, job, education, area, date;
    private int age, duration, members;

    public void createRecord(String gender, int age, String job, String edu, String area, String date, int duration, int members) {
        this.gender = gender;
        this.age = age;
        this.job = job;
        this.education = edu;
        this.area = area;
        this.date = date;
        this.duration = duration;
        this.members = members;
    }

    public String getGender() {
        return this.gender;
    }

    public int getAge() {
        return this.age;
    }

    public String getJob() {
        return this.job;
    }

    public String getEdu() {
        return this.education;
    }

    public String getArea() {
        return this.area;
    }

    public String getDate() {
        return this.date;
    }

    public int getDuration() {
        return this.duration;
    }

    public int getMembers() {
        return this.members;
    }

}
