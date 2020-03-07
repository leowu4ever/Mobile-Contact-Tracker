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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {

    private Button btnLogin, btnRegister;
    private EditText etUserName, etPassword;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUIs();
        getLocationPermission(this);

    }

    private void initUIs() {
        etUserName = findViewById(R.id.et_Main_userName);
        etPassword = findViewById(R.id.et_Main_Password);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {//Need to validate not null later
            @Override
            public void onClick(View v) {
                // password check
                String input = "{\"userName\": \"" + etUserName.getText().toString() + "\",\n" + "\"password\": \"" + etPassword.getText().toString() + "\n" + "}";

                DateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
                String sysDate = formatter.format(Calendar.getInstance().getTime());
                String PATH_LOCAL = Environment.getExternalStorageDirectory() + "/VirTrack/";

                File rootfolder = new File(PATH_LOCAL);
                if (!rootfolder.exists()) {
                    rootfolder.mkdir();
                }

                new File(rootfolder, "userdetails");

                try (FileWriter writer = new FileWriter(PATH_LOCAL + "userdetails")) {
                    writer.write(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
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
}
