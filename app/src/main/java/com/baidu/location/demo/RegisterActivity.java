package com.baidu.location.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.baidulocationdemo.R;

public class RegisterActivity extends Activity {

    private Button btnSubmit;

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

    }
}
