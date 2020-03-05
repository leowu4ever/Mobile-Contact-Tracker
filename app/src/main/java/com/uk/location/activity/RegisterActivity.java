package com.uk.location.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class RegisterActivity extends Activity {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Button btnSubmit, btn_Tnc;
    private SeekBar sb_age;
    private TextView tv_age;
    private EditText et_loginAccount, et_loginPassword;
    private Context context;

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
        setContentView(R.layout.activity_register);
        context = this;
        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getLocationPermission(context);
                Class<?> TargetClass = LocationActivity.class;
                Intent intent = new Intent(RegisterActivity.this, TargetClass);
                startActivity(intent);
            }
        });


        btn_Tnc = findViewById(R.id.btn_Tnc);
        btn_Tnc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*                Class<?> TargetClass = TermsnConditions.class;
                Intent intent = new Intent(RegisterActivity.this, TargetClass);
                startActivity(intent);*/
            }
        });

        et_loginAccount = findViewById(R.id.et_loginAccount);
        et_loginPassword = findViewById(R.id.et_loginPassword);

        sb_age = findViewById(R.id.sb_age);
        sb_age.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_age.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
}