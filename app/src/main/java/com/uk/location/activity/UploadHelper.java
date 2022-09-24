package com.uk.location.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class UploadHelper {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private StringBuilder stringToLog = new StringBuilder();

    public UploadHelper(String date, String time, String currentUser, double latitude, double longitude, int errorcode, String token) {
        String PATH_USER = Environment.getExternalStorageDirectory() + "/疫迹/" + currentUser + "/";
        JsonFileHelper fileReader = new JsonFileHelper(currentUser);
        Calendar currentTime = Calendar.getInstance();
        Gson g = new Gson();
        LocationLogData tempLog = new LocationLogData();

        String tempReadings = fileReader.readJsonFile(PATH_USER + "tempdata.json");
        ArrayList<String> tempLogDateList = new ArrayList<>();
        ArrayList<String> tempTimeList = new ArrayList<>();
        ArrayList<Double> tempLatitudeList = new ArrayList<>();
        ArrayList<Double> tempLongitudeList = new ArrayList<>();
        ArrayList<Integer> tempErrorcodeList = new ArrayList<>();
        LocationLogData tempLogData = g.fromJson(tempReadings, LocationLogData.class);
        stringToLog.append("\n").append(currentTime.get(Calendar.MONTH) + 1).append("_").append(currentTime.get(Calendar.DATE)).append("\n");


        if (!(tempReadings.equals(""))) {
            tempLogDateList = tempLogData.getDateList();
            tempTimeList = tempLogData.getTimeList();
            tempLatitudeList = tempLogData.getLatitudeList();
            tempLongitudeList = tempLogData.getLongitudeList();
            tempErrorcodeList = tempLogData.getErrorcodeLIst();
        }
        token = tokenRenewal(currentUser);
        if (new NetworkHelper().NetworkTest(currentUser) != null && token != null) {//if connection is possible
            File f = new File(PATH_USER + "tempdata.json");
            if (!(tempReadings.equals(""))) {
                try {//process existing tempdata
                    for (int i = 0; i < tempLogDateList.size(); i++) {
                        try {
                            String data = ("{\"username\": \"" + currentUser + "\",\"date\": \"" + tempLogDateList.get(i) + "\",\"time\": \"" + tempTimeList.get(i) + "\",\"longtitude\": \"" + tempLongitudeList.get(i) + "\",\"latitude\": \"" + tempLatitudeList.get(i) + "\",\"errorcode\": \"" + tempErrorcodeList.get(i) + "\"}");
                            String message = new NetworkHelper().CallAPI("POST", "post/statistics/location", data, token).get();
                            if (message == null || message.equals("") || !(message.equals("Success"))) {// if fail
                                tempLog.addLocationLogData(tempLogDateList.get(i), tempTimeList.get(i), tempLatitudeList.get(i), tempLongitudeList.get(i), tempErrorcodeList.get(i));
                                addToLog(" PAST fail ", tempLogDateList.get(i), tempTimeList.get(i), message);
                            } else {
                                addToLog(" PAST success ", tempLogDateList.get(i), tempTimeList.get(i), message);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            for (int y = i; y < tempLogDateList.size(); y++) {
                                tempLog.addLocationLogData(tempLogDateList.get(y), tempTimeList.get(y), tempLatitudeList.get(y), tempLongitudeList.get(y), tempErrorcodeList.get(y));
                                addToLog(" PAST BULK Exception ", tempLogDateList.get(y), tempTimeList.get(y), "");
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (f.delete()) {//remove temp file
                        System.out.println("File deleted successfully");
                        addToLog("  TEMP deleted  ", "", "", "");
                    } else {
                        System.out.println("Failed to delete the file");
                        addToLog("  TEMP NOT deleted  ", "", "", "");
                    }
                }
            }

            try {
                new NetworkHelper().CallAPI("POST", "post/statistics/location", "{}", token).get();
                String data = ("{\"username\": \"" + currentUser + "\",\"date\": \"" + date + "\",\"time\": \"" + time + "\",\"longtitude\": \"" + longitude + "\",\"latitude\": \"" + latitude + "\",\"errorcode\": \"" + errorcode + "\"}");
                System.out.println(data);
                String message = new NetworkHelper().CallAPI("POST", "post/statistics/location", data, token).get();
                if (message == null || message.equals("") || !(message.equals("Success"))) {// if fail
                    tempLog.addLocationLogData(date, time, latitude, longitude, errorcode);
                    addToLog(" TODAY fail ", date, time, "");
                } else {
                    addToLog(" TODAY success ", date, time, "");
                }
            } catch (Exception e) {//if unable to upload today data, add remainings to tempdata file
                e.printStackTrace();
                tempLog.addLocationLogData(date, time, latitude, longitude, errorcode);
                addToLog(" TODAY BULK exception ", date, time, "");
            }

        } else {//if connection is unable to establish convert data in today's file to object and add to tempdata
            tempLog.addLocationLogData(date, time, latitude, longitude, errorcode);
            addToLog(" CONNECT FAIL OBJ to FILE ", date, time, "");
        }//if tempdata exist
        if (tempLog.getDateList().size() > 0 || tempLog.getTimeList().size() > 0 || tempLog.getLatitudeList().size() > 0 || tempLog.getLongitudeList().size() > 0 || tempLog.getErrorcodeLIst().size() > 0) {
            File rootfolder = new File(PATH_USER);
            if (!rootfolder.exists()) {
                rootfolder.mkdir();
            }
            new File(rootfolder, "tempdata.json");

            try (FileWriter writer = new FileWriter(PATH_USER + "/tempdata.json")) {
                g.toJson(tempLog, writer);//convert to json and save
                addToLog(" TEMP created","" , "", "");
            } catch (IOException e) {
                e.printStackTrace();
                addToLog(" TEMP create FAIL","" , "", "");
            }
        }
        System.out.println("FINISHED");
        LOGLOG(currentUser, stringToLog.toString());
    }


    private String tokenRenewal(String userName) {
        Calendar currentTime = Calendar.getInstance();
        RegistrationData registrationData = new Gson().fromJson(new JsonFileHelper(userName).readJsonFile(Environment.getExternalStorageDirectory() + "/疫迹/autologin_userdetails.json"), RegistrationData.class);
        String usernameFromLog = registrationData.getUserName();
        String passwordFromLog = registrationData.getPassword();
        String data = "{\"username\":\"" + usernameFromLog + "\",\"password\":\"" + passwordFromLog + "\"}";
        try {
            String TOKEN = new NetworkHelper().CallAPI("POST", "authentication/login", data, "").get();
            addToLog(" TOKEN","" , "", TOKEN);
            return TOKEN;
        } catch (Exception e) {
            addToLog(" TOKEN EXCEPTION","" , "", "");
            return null;
        }
    }

    private void LOGLOG(String currentUser, String data) {
        String PATH_USER = Environment.getExternalStorageDirectory() + "/疫迹/" + currentUser + "/";
        File loglog = new File(PATH_USER + "LogLog.txt");
        System.out.println("DDDRRR");
        try (FileWriter writer = new FileWriter(PATH_USER + "LogLog.txt", true)) {
            writer.write(data);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
    }

    private void addToLog(String identifer, String date, String time, String message){
        Calendar currentTime = Calendar.getInstance();
        stringToLog.append(currentTime.get(Calendar.HOUR_OF_DAY)).append(":").append(currentTime.get(Calendar.MINUTE)).append(":").append(currentTime.get(Calendar.SECOND)).append(identifer).append(" ").append(date).append(" ").append(time).append(" ").append(message).append("\n");
    }
}
