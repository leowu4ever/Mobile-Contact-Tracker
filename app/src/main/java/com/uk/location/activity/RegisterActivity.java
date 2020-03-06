package com.uk.location.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterActivity extends Activity {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private Button btnSubmit, btn_privacy;
    private SeekBar sb_age;
    private TextView tv_age;
    private EditText et_loginAccount, et_loginPassword;
    private Context context;
    private Spinner sp_occupation, sp_edu;
    private String[] sp_item_job = {"服务业", "农民/工人", "医务人员", "退休人员", "其他"};
    private String[] sp_item_education = {"小学或以下", "初中", "高中", "大学", "硕士", "博士或其他"};
    private Dialog privacyDialog;

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
        tv_age = findViewById(R.id.tv_age);
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


        btn_privacy = findViewById(R.id.btn_privacy);
        btn_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacyDialog = new Dialog(context);
                privacyDialog.setContentView(R.layout.dialog_privacy);
                privacyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btn_privacy_return = privacyDialog.findViewById(R.id.btn_return);
                btn_privacy_return.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        privacyDialog.dismiss();
                    }
                });
                privacyDialog.show();
            }
        });

        et_loginAccount = findViewById(R.id.et_loginAccount);
        et_loginPassword = findViewById(R.id.et_loginPassword);

        sb_age = findViewById(R.id.sb_age);
        sb_age.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_age.setText("" + progress + "岁");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sp_occupation = findViewById(R.id.sp_occupation3);
        ArrayAdapter aa_occupation = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sp_item_job);
        aa_occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_occupation.setAdapter(aa_occupation);

        sp_edu = findViewById(R.id.sp_edu2);
        ArrayAdapter aa_edu = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sp_item_education);
        aa_edu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_edu.setAdapter(aa_edu);


    }


}