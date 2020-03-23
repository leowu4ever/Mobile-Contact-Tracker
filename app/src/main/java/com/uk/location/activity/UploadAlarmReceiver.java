package com.uk.location.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

public class UploadAlarmReceiver extends BroadcastReceiver {

    private final String ALARM_ACTION_LABEL = "UPLOAD_ALARM";
    private AlarmManager am;
    private Intent intent;
    private PendingIntent pi;
    private PowerManager pm;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    public void onReceive(Context context, Intent intent) {
        String userID = intent.getStringExtra("USER_ID");
        String userToken = intent.getStringExtra("USER_TOKEN");

        System.out.println("VVV"+userID);
        System.out.println("VVV"+userToken);

        alarm(userID, userToken);
    }

    public void startAlarm(String currentID, String token, Context context) {
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, UploadAlarmReceiver.class);
        intent.putExtra("USER_ID", currentID);
        intent.putExtra("USER_TOKEN", token);

        System.out.println("LLL"+currentID);
        System.out.println("LLL"+token);


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 20);
        calendar.set(Calendar.MINUTE, 00);

        if(calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
        }

        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, Intent.FILL_IN_DATA);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public void alarm(String currentID, String token) {
        String PATH_USER = Environment.getExternalStorageDirectory() + "/疫迹/" + currentID + "/";
        JsonFileHelper fileReader = new JsonFileHelper(currentID);
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

        if (new NetworkHelper().NetworkTest(currentID) != null && token != null) {//if connection is possible
            token = tokenRenewal(currentID);
            System.out.println("DDDPDDD");
            File f = new File(PATH_USER + "tempdata");
            if (f.exists()) {
                try {//process existing tempdata
                    String tempReadings = fileReader.readJsonFile(PATH_USER + "tempdata");
                    if (tempReadings != null) {
                        LocationLogData tempLogData = g.fromJson(tempReadings, LocationLogData.class);
                        ArrayList<String> tempLogDateList = tempLogData.getDateList();
                        ArrayList<String> tempTimeList = tempLogData.getTimeList();
                        ArrayList<Double> tempLatitudeList = tempLogData.getLatitudeList();
                        ArrayList<Double> tempLongitudeList = tempLogData.getLongitudeList();
                        ArrayList<Integer> tempErrorcodeList = tempLogData.getErrorcodeLIst();
                        for (int i = 0; i < tempLogDateList.size(); i++) {
                            try {
                                String data = ("{\"username\": \"" + currentID + "\",\"date\": \"" + tempLogDateList.get(i) + "\",\"time\": \"" + tempTimeList.get(i) + "\",\"longtitude\": \"" + tempLongitudeList.get(i) + "\",\"latitude\": \"" + tempLatitudeList.get(i) + "\",\"errorcode\": \"" + tempErrorcodeList.get(i) + "\"}");
                                String message = new NetworkHelper().CallAPI("POST", "post/statistics/location", data, token).get();
                                if (!(message.equals("")) && !(message.equals("Success"))) {
                                    System.out.println("DDDPast tempdata " + i + " fail to upload");
                                    tempLog.addLocationLogData(tempLogDateList.get(i), tempTimeList.get(i), tempLatitudeList.get(i), tempLongitudeList.get(i), tempErrorcodeList.get(i));
                                } else {
                                    System.out.println("DDDPast tempdata " + i + " UPLOADED");
                                }
                            }catch(Exception e){
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
                    if (f.delete()) {
                        System.out.println("File deleted successfully");
                    } else {
                        System.out.println("Failed to delete the file");
                    }
                }
            }

            for (int i = 0; i < logDateList.size(); i++) {//process today data
                try {
                    String data = ("{\"username\": \"" + currentID + "\",\"date\": \"" + logDateList.get(i) + "\",\"time\": \"" + timeList.get(i) + "\",\"longtitude\": \"" + longitudeList.get(i) + "\",\"latitude\": \"" + latitudeList.get(i) + "\",\"errorcode\": \"" + errorcodeList.get(i) + "\"}");
                    String message = new NetworkHelper().CallAPI("POST", "post/statistics/location", data, token).get();
                    if (!(message.equals("Success"))) {
                        tempLog.addLocationLogData(logDateList.get(i), timeList.get(i), latitudeList.get(i), longitudeList.get(i), errorcodeList.get(i));
                        System.out.println("DDDToday data "+i+"failed to send");
                    }else{
                        System.out.println("DDDToday data "+i+"sent");
                    }

                } catch (Exception e) {//if unable to upload today data, add remainings to tempdata file
                    e.printStackTrace();
                    for (int y = i; y < logDateList.size(); y++) {
                        tempLog.addLocationLogData(logDateList.get(y), timeList.get(y), latitudeList.get(y), longitudeList.get(y), errorcodeList.get(y));
                        System.out.println("DDDToday data fail to send "+ logDateList.size() +"in total");
                    }
                    break;
                }
            }

        } else {//if connection is unable to establish
            for (int i = 0; i < logDateList.size(); i++) {//convert data to object and store as tempdata
                tempLog.addLocationLogData(logDateList.get(i), timeList.get(i), latitudeList.get(i), longitudeList.get(i), errorcodeList.get(i));
            }
        }
        if (tempLog.getDateList().size() > 0 || tempLog.getTimeList().size() > 0 || tempLog.getLatitudeList().size() > 0 || tempLog.getLongitudeList().size() > 0 || tempLog.getErrorcodeLIst().size() > 0) {
            File rootfolder = new File(PATH_USER);
            if (!rootfolder.exists()) {
                rootfolder.mkdir();
            }
            new File(rootfolder, "tempdata");

            try (FileWriter writer = new FileWriter(PATH_USER + "/tempdata")) {
                g.toJson(tempLog, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String tokenRenewal(String userName) {
        RegistrationData registrationData = new JsonFileHelper(userName).readRegistrationDataFromLocal();
        String passwordFromLog = registrationData.getPassword();
        String data = "{\"username\":\"" + userName + "\",\"password\":\"" + passwordFromLog + "\"}";
        try{
        return new NetworkHelper().CallAPI("POST", "authentication/login", data, "").get();
        }catch(Exception e){return null;}
    }
}
