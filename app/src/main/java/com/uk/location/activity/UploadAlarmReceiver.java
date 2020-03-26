package com.uk.location.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class UploadAlarmReceiver extends BroadcastReceiver {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public void onReceive(Context context, Intent intent) {
        String userID = intent.getStringExtra("USER_ID");
        String userToken = intent.getStringExtra("USER_TOKEN");

        alarm(userID, userToken);
    }

    public void startAlarm(String currentUser, String token, Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, UploadAlarmReceiver.class);
        intent.putExtra("USER_ID", currentUser);
        intent.putExtra("USER_TOKEN", token);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 03);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, Intent.FILL_IN_DATA);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public void alarm(String currentUser, String token) {
        String PATH_USER = Environment.getExternalStorageDirectory() + "/疫迹/" + currentUser + "/";
        JsonFileHelper fileReader = new JsonFileHelper(currentUser);
        Calendar currentTime = Calendar.getInstance();
        Gson g = new Gson();
        LocationLogData tempLog = new LocationLogData();
        System.out.println(currentTime.get((Calendar.MONTH)) + "_" + currentTime.get(Calendar.DATE) + "_location_log.json");
        String readings = fileReader.readJsonFile(PATH_USER + (currentTime.get(Calendar.MONTH) + 1) + "_" + currentTime.get(Calendar.DATE) + "_location_log.json");

        LocationLogData logData = g.fromJson(readings, LocationLogData.class);
        ArrayList<String> logDateList = logData.getDateList();
        ArrayList<String> timeList = logData.getTimeList();
        ArrayList<Double> latitudeList = logData.getLatitudeList();
        ArrayList<Double> longitudeList = logData.getLongitudeList();
        ArrayList<Integer> errorcodeList = logData.getErrorcodeLIst();

        if (new NetworkHelper().NetworkTest(currentUser) != null && token != null) {//if connection is possible
            token = tokenRenewal(currentUser);
            if (token != null) {
                File f = new File(PATH_USER + "tempdata.json");
                if (f.exists()) {
                    try {//process existing tempdata
                        String tempReadings = fileReader.readJsonFile(PATH_USER + "tempdata.json");
                        if (tempReadings != null) {
                            LocationLogData tempLogData = g.fromJson(tempReadings, LocationLogData.class);
                            ArrayList<String> tempLogDateList = tempLogData.getDateList();
                            ArrayList<String> tempTimeList = tempLogData.getTimeList();
                            ArrayList<Double> tempLatitudeList = tempLogData.getLatitudeList();
                            ArrayList<Double> tempLongitudeList = tempLogData.getLongitudeList();
                            ArrayList<Integer> tempErrorcodeList = tempLogData.getErrorcodeLIst();
                            for (int i = 0; i < tempLogDateList.size(); i++) {
                                try {
                                    String data = ("{\"username\": \"" + currentUser + "\",\"date\": \"" + tempLogDateList.get(i) + "\",\"time\": \"" + tempTimeList.get(i) + "\",\"longtitude\": \"" + tempLongitudeList.get(i) + "\",\"latitude\": \"" + tempLatitudeList.get(i) + "\",\"errorcode\": \"" + tempErrorcodeList.get(i) + "\"}");
                                    String message = new NetworkHelper().CallAPI("POST", "post/statistics/location", data, token).get();
                                    if (!(message.equals("")) && !(message.equals("Success"))) {// if fail
                                        tempLog.addLocationLogData(tempLogDateList.get(i), tempTimeList.get(i), tempLatitudeList.get(i), tempLongitudeList.get(i), tempErrorcodeList.get(i));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    for (int j = i; j < tempLogDateList.size(); j++) {
                                        tempLog.addLocationLogData(tempLogDateList.get(j), tempTimeList.get(j), tempLatitudeList.get(j), tempLongitudeList.get(j), tempErrorcodeList.get(j));
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (f.delete()) {//remove temp file
                            System.out.println("File deleted successfully");
                        } else {
                            System.out.println("Failed to delete the file");
                        }
                    }
                }

                if (readings != null) {//if today have data
                    for (int i = 0; i < logDateList.size(); i++) {//process today data
                        try {
                            String data = ("{\"username\": \"" + currentUser + "\",\"date\": \"" + logDateList.get(i) + "\",\"time\": \"" + timeList.get(i) + "\",\"longtitude\": \"" + longitudeList.get(i) + "\",\"latitude\": \"" + latitudeList.get(i) + "\",\"errorcode\": \"" + errorcodeList.get(i) + "\"}");
                            System.out.println(data);
                            String message = new NetworkHelper().CallAPI("POST", "post/statistics/location", data, token).get();
                            if (!(message.equals("Success"))) {// if fail
                                tempLog.addLocationLogData(logDateList.get(i), timeList.get(i), latitudeList.get(i), longitudeList.get(i), errorcodeList.get(i));
                            }
                        } catch (Exception e) {//if unable to upload today data, add remainings to tempdata file
                            e.printStackTrace();
                            for (int y = i; y < logDateList.size(); y++) {
                                tempLog.addLocationLogData(logDateList.get(y), timeList.get(y), latitudeList.get(y), longitudeList.get(y), errorcodeList.get(y));
                            }
                            break;
                        }
                    }
                }
            }
        } else {//if connection is unable to establish
            for (int i = 0; i < logDateList.size(); i++) {//convert data in today's file to object and add to tempdata
                tempLog.addLocationLogData(logDateList.get(i), timeList.get(i), latitudeList.get(i), longitudeList.get(i), errorcodeList.get(i));
            }
        }//if tempdata exist
        if (tempLog.getDateList().size() > 0 || tempLog.getTimeList().size() > 0 || tempLog.getLatitudeList().size() > 0 || tempLog.getLongitudeList().size() > 0 || tempLog.getErrorcodeLIst().size() > 0) {
            File rootfolder = new File(PATH_USER);
            if (!rootfolder.exists()) {
                rootfolder.mkdir();
            }
            new File(rootfolder, "tempdata.json");

            try (FileWriter writer = new FileWriter(PATH_USER + "/tempdata.json")) {
                g.toJson(tempLog, writer);//convert to json and save
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String tokenRenewal(String userName) {
        RegistrationData registrationData = new Gson().fromJson(new JsonFileHelper(userName).readJsonFile(Environment.getExternalStorageDirectory() + "/疫迹/autologin_userdetails.json"), RegistrationData.class);
        String usernameFromLog = registrationData.getUserName();
        String passwordFromLog = registrationData.getPassword();
        String data = "{\"username\":\"" + usernameFromLog + "\",\"password\":\"" + passwordFromLog + "\"}";
        try {
            return new NetworkHelper().CallAPI("POST", "authentication/login", data, "").get();
        } catch (Exception e) {
            return null;
        }
    }
}
