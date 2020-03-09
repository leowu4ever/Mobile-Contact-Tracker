package com.uk.location.activity;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Record {
    private String gender, job, education, area, date, time, duration, members;
    private int age;

    public void createRecord(String gender, int age, String job, String edu, String area, String date, String time, String duration, String members) {
        this.gender = gender;
        this.age = age;
        this.job = job;
        this.education = edu;
        this.area = area;
        this.date = date;
        this.time = time;
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

    public String getTime() {
        return this.time;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getMembers() {
        return this.members;
    }

    public void objectToFile(Record record, String dateForFile) {

        DateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
        String sysDate = formatter.format(Calendar.getInstance().getTime());
        Gson gson = new Gson();
        String PATH_LOCAL = Environment.getExternalStorageDirectory() + "/VirTrack/";

        File rootfolder = new File(PATH_LOCAL);
        if (!rootfolder.exists()) {
            rootfolder.mkdir();
        }

        new File(rootfolder, "record_" + dateForFile + sysDate);

        try (FileWriter writer = new FileWriter(PATH_LOCAL + "record_" + dateForFile + sysDate)) {
            gson.toJson(record, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
