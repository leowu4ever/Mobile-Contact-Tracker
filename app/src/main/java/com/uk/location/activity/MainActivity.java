package com.uk.location.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends Activity {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Button btnLogin, btnRegister;
    private EditText etUserName, etPassword;
    private static String currentUser, token;

    public static void getLocationPermission(Context context) {
        // TO DO need permission check on later screens
        if (ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            String[] permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.ACCESS_WIFI_STATE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions((Activity) context, permissions, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        initUIs();
        getLocationPermission(this);
        autoLogin();
        currentUser = "";
        token = "";
    }

    private void initUIs() {
        etUserName = findViewById(R.id.et_main_username);
        etPassword = findViewById(R.id.et_main_password);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {//Need to validate not null later
            @Override
            public void onClick(View v) {
                if (!(etUserName.getText().toString().matches("")) && !(etPassword.getText().toString().matches(""))) {
                    String data = "{\"username\":\"" + etUserName.getText().toString() + "\",\"password\":\"" + etPassword.getText().toString() + "\"}";
                    NetworkHelper nh = new NetworkHelper();
                    String returnText = "";
                    try {
                        returnText = nh.CallAPI("POST", "authentication/login", data, "").get();
                    }catch(Exception e){
                        returnText = "Error";
                    }
                    if (returnText.contains("Error")||returnText.equals("")) {
                        Toast.makeText(getApplicationContext(), "登录失敗, 請重試", Toast.LENGTH_SHORT).show();
                    }else{
                        RegistrationData login = new RegistrationData(etUserName.getText().toString(),etPassword.getText().toString());
                        currentUser = etUserName.getText().toString();
                        token = returnText;
                        Gson gson = new Gson();

                        String PATH_LOCAL = Environment.getExternalStorageDirectory() + "/疫迹";

                        File rootfolder = new File(PATH_LOCAL);
                        if (!rootfolder.exists()) {
                            rootfolder.mkdir();
                        }
                        new File(rootfolder, "autologin_userdetails.json");

                        try (FileWriter writer = new FileWriter(PATH_LOCAL + "/autologin_userdetails.json")) {
                            gson.toJson(login, writer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        startAcivity();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "请填写全部信息", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterDialog(MainActivity.this);
            }
        });
    }

    private void autoLogin() {
        String readings = "";
        FileInputStream fis = null;
        try {
            File f = new File(Environment.getExternalStorageDirectory() + "/疫迹/autologin_userdetails.json");
            fis = new FileInputStream(f);  // 2nd line
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String readinText = "";

            while ((readinText = br.readLine()) != null) {
                sb.append(readinText);
            }
            readings = sb.toString();
            Gson gson = new Gson();
            RegistrationData logindetails = gson.fromJson(readings, RegistrationData.class);
            currentUser = logindetails.getUserName();
            String data = "{\"username\":\"" + logindetails.getUserName() + "\",\"password\":\"" + logindetails.getPassword() + "\"}";
            NetworkHelper nh = new NetworkHelper();
            String returnText = "";

            try {
                returnText = nh.CallAPI("POST", "authentication/login", data, "").get();
            }catch(Exception e){
                returnText = "Error";
            }

            if (returnText.contains("Error") || (returnText.equals(""))) {
                Toast.makeText(getApplicationContext(), "登录失敗, 請重試", Toast.LENGTH_SHORT).show();
                if (f.delete()) {
                    System.out.println("File deleted successfully");
                } else {
                    System.out.println("Failed to delete the file");
                }
            } else {
                startAcivity();
            }

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
    }

    private void startAcivity() {
        Intent intent = new Intent(MainActivity.this, LocationActivity.class);
        intent.putExtra("USER_ID", currentUser);
        intent.putExtra("USER_TOKEN", token);
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
    }
}
