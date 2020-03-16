package com.uk.location.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
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
    private static String currentUser;

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
        setContentView(R.layout.activity_main);
        initUIs();
        getLocationPermission(this);
        autoLogin();
        currentUser = "";
    }

    private void initUIs() {
        etUserName = findViewById(R.id.et_main_username);
        etPassword = findViewById(R.id.et_main_password);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {//Need to validate not null later
            @Override
            public void onClick(View v) {
                if (!(etUserName.getText().toString().matches("")) && !(etPassword.getText().toString().matches(""))) {
                    // password check
                    //if success
                    Login login = new Login();
                    login.setUsername(etUserName.getText().toString());
                    login.setPassword(etPassword.getText().toString());
                    currentUser = etUserName.getText().toString();
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
                } else {
                    Toast.makeText(getApplicationContext(), "登录失敗, 請重試", Toast.LENGTH_SHORT).show();
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
            fis = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/疫迹/autologin_userdetails.json"));  // 2nd line
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String readinText = "";

            while ((readinText = br.readLine()) != null) {
                sb.append(readinText);
            }
            readings = sb.toString();
            Gson gson = new Gson();
            Login logindetails = gson.fromJson(readings, Login.class);
            currentUser = logindetails.getUsername();
            //Login checks
            startAcivity();

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
        startActivity(intent);
        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
    }

    public static void setCurrentUser(String user) {
        currentUser = user;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    class Login {
        private String username;
        private String password;

        private void setUsername(String input) {
            username = input;
        }

        private String getUsername() {
            return username;
        }

        private void setPassword(String input) {
            password = input;
        }

        private String getPassword() {
            return password;
        }
    }
}
