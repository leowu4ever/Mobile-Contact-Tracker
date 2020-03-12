package com.uk.location.activity;

import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LogLocation {

    public void LogLocation(String input){
//        LogLocation.Location loc = new LogLocation.Location(lon, lat , time, isTracking);

        Gson gson = new Gson();

        String PATH_LOCAL = Environment.getExternalStorageDirectory() + "/VirTrack/";

        File rootfolder = new File(PATH_LOCAL);
        if (!rootfolder.exists()) {
            rootfolder.mkdir();
        }

        new File(rootfolder, "locationdetails.txt");

        try (FileWriter writer = new FileWriter(PATH_LOCAL + "locationdetails.txt",true)) {
            writer.write(input);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*class Location {
        private String longitude;
        private String latitude;
        private String time;
        private boolean isTracking;

        public Location(String lon, String lat, String time, boolean isTracking){
            this.longitude = lon;
            this.latitude = lat;
            this.time = time;
            this.isTracking = false;
        };

        private void setTrackingState(Boolean input) {
            isTracking = input;
        }
        private boolean getTrackingState() {
            return isTracking;
        }
    }*/
}