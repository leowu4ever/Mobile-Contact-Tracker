package com.baidu.location.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.baidulocationdemo.R;

public class RegisterActivity extends Activity {

    private Button btnSubmit, btn_Tnc;
    private SeekBar sb_age;
    private TextView tv_age;
    private EditText et_loginAccount, et_loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
