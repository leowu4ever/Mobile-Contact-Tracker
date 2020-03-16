package com.uk.location.activity;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;


public class JsonFileHelper {

    // - app folder
    //   - user 1
    //       - registration information
    //       - location log 1 (year_month_day_location_log.json)

    public final String PATH_ROOT_FOLDER = Environment.getExternalStorageDirectory() + "/疫迹";
    public String PATH_USER_FOLDER;
    public final String LOCATION_LOG_FILENAME_SUFFIX = "_location_log.json";
    public final String REGISTRATION_FILE_NAME = "registration.json";
    public String REGISTRATION_FILE_PATH;

    public JsonFileHelper(String userName) {
        PATH_USER_FOLDER = PATH_ROOT_FOLDER + "/" + userName + "/";
        REGISTRATION_FILE_PATH = PATH_USER_FOLDER + REGISTRATION_FILE_NAME;
        initDir();
    }


    public void saveRegistrationDataFromLocal(RegistrationData registrationData) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(REGISTRATION_FILE_PATH)) {
            gson.toJson(registrationData, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RegistrationData readRegistrationDataFromLocal() {
        File overallDataFile = new File(REGISTRATION_FILE_PATH);
        if (!overallDataFile.exists()) {
            RegistrationData emptyRegistrationData = new RegistrationData(null,null);
            saveRegistrationDataFromLocal(emptyRegistrationData);
        }

        Gson g = new Gson();
        String readings = readJsonFile(REGISTRATION_FILE_PATH);
        return g.fromJson(readings, RegistrationData.class);
    }

    public void saveLocationLogDataToLocal(LocationLogData locationLog) {
        Calendar currentTime = Calendar.getInstance();
        String date = (currentTime.get(Calendar.MONTH)+1)+ "_" + currentTime.get(Calendar.DATE);
        String path = PATH_USER_FOLDER + date + LOCATION_LOG_FILENAME_SUFFIX;
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(locationLog, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public LocationLogData readLocationLogDataFromLocal() {
        Calendar currentTime = Calendar.getInstance();
        String date = (currentTime.get(Calendar.MONTH)+1) + "_" + currentTime.get(Calendar.DATE);
        String path = PATH_USER_FOLDER + date + LOCATION_LOG_FILENAME_SUFFIX;
        File overallDataFile = new File(path);
        if (!overallDataFile.exists()) {
            LocationLogData emptyLocationLog = new LocationLogData();
            saveLocationLogDataToLocal(emptyLocationLog);
        }

        Gson g = new Gson();
        String readings = readJsonFile(path);
        return g.fromJson(readings, LocationLogData.class);
    }

    public String readJsonFile(String filePath) {
        String readings = "";
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(filePath));  // 2nd line
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String readinText = "";

            while ((readinText = br.readLine()) != null) {
                sb.append(readinText);
            }
            readings = sb.toString();
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return readings;
    }

    public void initDir() {
        File rootfolder = new File(PATH_ROOT_FOLDER);
        if (!rootfolder.exists()) {
            rootfolder.mkdir();
        }
        File userFolder = new File(PATH_USER_FOLDER);
        if (!userFolder.exists()) {
            userFolder.mkdir();
        }
    }
}
