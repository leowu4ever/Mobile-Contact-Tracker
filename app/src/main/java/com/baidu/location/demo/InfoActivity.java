package com.baidu.location.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.baidulocationdemo.R;

public class InfoActivity extends Activity {

    private Button btnSubmit;
    private Spinner sp_healthState;
    private SeekBar sb_age;
    private TextView tv_age;
    private String[] sp_Item_HealthState = {"差劣", "略差", "一般", "良好", "優良"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);


        sp_healthState = findViewById(R.id.sp_healthState);
        ArrayAdapter aa_healthS = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sp_Item_HealthState);
        aa_healthS.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_healthState.setAdapter(aa_healthS);

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class<?> TargetClass = LocationActivity.class;
                Intent intent = new Intent(InfoActivity.this, TargetClass);
                startActivity(intent);
            }
        });

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
