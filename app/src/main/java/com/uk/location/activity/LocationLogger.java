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

public class LocationLogger {
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

    public void LogLocationViaObject(String lat, String lon, String date, String time) {
        Gson gson = new Gson();

        File rootDircectory = new File(PATH_LOCAL);
        if (!rootDircectory.exists()) {
            rootDircectory.mkdir();
        }

        String fileobjname = "locationdetails_obj.txt";

        new File(rootDircectory, fileobjname);

        FileHelper fh = new FileHelper();

        String[] files = rootDircectory.list();

        List<String> colList = new ArrayList(Arrays.asList(files));
        List<String> workableList = new ArrayList();
        for (String str : colList) {
            if (str.startsWith("locationdetails_")) {
                workableList.add(str);
            }
        }
        if (workableList.size() > 0) {
            String readings = fh.readFile("locationdetails_obj.txt");
            loco = gson.fromJson(readings, Location.class);
        }
        loco.addTripData(lat, lon, date, time);

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
        private ArrayList<String> lat;
        private ArrayList<String> lon;
        private ArrayList<String> date;
        private ArrayList<String> time;

        public Location(boolean isTracking) {
            this.isTracking = false;
            lat = new ArrayList<String>();
            lon = new ArrayList<String>();
            date = new ArrayList<String>();
            time = new ArrayList<String>();
        }

        private void setTrackingState(Boolean input) {
            isTracking = input;
        }

        private boolean getTrackingState() {
            return isTracking;
        }

        private void addTripData(String lat, String lon, String date, String time) {
            this.lat.add(lat);
            this.lon.add(lon);
            this.date.add(date);
            this.time.add(time);
        }
    }
}