package com.uk.location.activity;

import android.os.Environment;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class LogLocation {
    Location loco = new Location(false);
    Calendar currentTime = Calendar.getInstance();
    String PATH_LOCAL = Environment.getExternalStorageDirectory() + "/VirTrack/";

    public void LogLocation(double lan, double lon, String time) {
        File rootDircectory = new File(PATH_LOCAL);
        if (!rootDircectory.exists()) {
            rootDircectory.mkdir();
        }

        String[] files = rootDircectory.list();

        List<String> colList = new ArrayList(Arrays.asList(files));
        List<String> workableList = new ArrayList();
        for (String str : colList) {
            if (str.startsWith("locationdetails_")) {
                workableList.add(str);
            }
        }

        Collections.sort(workableList);

        String filename = "";
        String newLine = "";
        String readings = "";

        if (workableList.size() == 0) {
            filename = "locationdetails_" + currentTime.get(Calendar.HOUR) +
                    currentTime.get(Calendar.MINUTE) +
                    currentTime.get(Calendar.SECOND) + ".txt";
            File target = new File(rootDircectory, filename);
        } else {
            filename = workableList.get(0);
            File target = new File(rootDircectory, filename);
            try (FileReader fr = new FileReader(PATH_LOCAL + filename)) {
                int i;
                while ((i = fr.read()) != -1)
                    readings += (char) i;
            } catch (Exception e) {
                Log.e("login activity", "File not found: " + e.toString());
            }
        }

        String starting = "";
        int tripNo = 1;
        if (readings.length() == 0) {
            newLine = "{\"isTracking\":false, \"Log\":{";
        } else {
            newLine = readings.substring(0, readings.length() - 2);
            starting = ",";
            tripNo = (newLine.length() - newLine.replace("Trip", "").length()) / "Trip".length() + 1;
        }

        newLine += starting + "\"Trip" + tripNo + "\":{\"lat\":\"" + lan + "\", \"long\":\"" + lon + "\",\"time\":\"" + time + "\" }}}";

        try (FileWriter writer = new FileWriter(PATH_LOCAL + filename)) {
            writer.write(newLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void LogLocationViaObject() {
        Gson gson = new Gson();

        File rootfolder = new File(PATH_LOCAL);
        if (!rootfolder.exists()) {
            rootfolder.mkdir();
        }

        String fileobjname = "locationdetails_obj_" + currentTime.get(Calendar.HOUR) + currentTime.get(Calendar.MINUTE) + currentTime.get(Calendar.SECOND) + ".txt";

        new File(rootfolder, fileobjname);

        try (FileWriter writer = new FileWriter(PATH_LOCAL + fileobjname)) {
            gson.toJson(loco, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addLocationToObject(String lat, String lon, String date, String time) {
        loco.addTripData(lat, lon, date, time);
    }

    class Location {
        private boolean isTracking;
        private ArrayList<trip> tripData;

        public Location(boolean isTracking) {
            this.isTracking = false;
            tripData = new ArrayList<trip>();
        }

        private void setTrackingState(Boolean input) {
            isTracking = input;
        }

        private boolean getTrackingState() {
            return isTracking;
        }

        private void addTripData(String lat, String lon, String date, String time) {
            tripData.add(new trip(lat, lon, date, time));
        }
    }

    class trip {
        private String lat;
        private String lon;
        private String date;
        private String time;

        public trip(String lat, String lon, String date, String time) {
            setLat(lat);
            setLong(lon);
            setDate(date);
            setTime(time);
        }

        private void setLat(String lat) {
            this.lat = lat;
        }

        private void setLong(String lon) {
            this.lon = lon;
        }

        private void setDate(String lon) {
            this.lon = lon;
        }

        private void setTime(String time) {
            this.time = time;
        }
    }
}