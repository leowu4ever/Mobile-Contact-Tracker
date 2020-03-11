package com.uk.location.activity;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogLocation {
    public void LogLocation(String lon, String lat, String time, boolean istracking){
        LogLocation.Location loc = new LogLocation.Location(lon);

        Gson gson = new Gson();

        String PATH_LOCAL = Environment.getExternalStorageDirectory() + "/VirTrack/";

        File rootfolder = new File(PATH_LOCAL);
        if (!rootfolder.exists()) {
            rootfolder.mkdir();
        }

        new File(rootfolder, "locationdetails.txt");

        try (FileWriter writer = new FileWriter(PATH_LOCAL + "locationdetails.txt")) {
            gson.toJson(loc, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class Location {
        private String longitude;
        private String latitude;
        private String time;
        private boolean isTracking;

        public Location(String lon){
            this.longitude = lon;
        };

        private void setTrackingState(Boolean input) {
            isTracking = input;
        }
        private boolean getTrackingState() {
            return isTracking;
        }
    }
}