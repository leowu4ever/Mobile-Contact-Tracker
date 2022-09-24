package com.uk.location.activity;

import java.util.ArrayList;

public class LocationLogData {

    public boolean uploaded;
    public ArrayList<String> dateList;
    public ArrayList<String> timeList;
    public ArrayList<Double> latitudeList;
    public ArrayList<Double> longitudeList;
    public ArrayList<Integer> errorcodeLIst;

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public ArrayList<String> getDateList() {
        return dateList;
    }

    public void addDateList(String date) {
        this.dateList.add(date);
    }

    public ArrayList<String> getTimeList() {
        return timeList;
    }

    public void addTimeList(String time) {
        this.timeList.add(time);
    }

    public ArrayList<Double> getLatitudeList() {
        return latitudeList;
    }

    public void addLatitudeList(Double latitude) {
        this.latitudeList.add(latitude);
    }

    public ArrayList<Double> getLongitudeList() {
        return longitudeList;
    }

    public void addLongitudeList(Double longitude) {
        this.longitudeList.add(longitude);
    }

    public ArrayList<Integer> getErrorcodeLIst() {
        return errorcodeLIst;
    }

    public void addErrorcodeLIst(int errorcode) {
        this.errorcodeLIst.add(errorcode);
    }

    public LocationLogData() {

        uploaded = false;
        dateList = new ArrayList<>();
        timeList = new ArrayList<>();
        latitudeList = new ArrayList<>();
        longitudeList = new ArrayList<>();
        errorcodeLIst = new ArrayList<>();
    }

    public void addLocationLogData (String date, String time, double latitude, double longitude, int errorcode) {
        addDateList(date);
        addTimeList(time);
        addLatitudeList(latitude);
        addLongitudeList(longitude);
        addErrorcodeLIst(errorcode);
    }

}
