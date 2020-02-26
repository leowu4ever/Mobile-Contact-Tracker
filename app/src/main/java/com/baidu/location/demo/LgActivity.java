package com.baidu.location.demo;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.baidulocationdemo.R;

public class LgActivity extends Activity {

    private Button btnAgree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lg);
        btnAgree = findViewById(R.id.btn_agree);
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Class<?> TargetClass = InfoActivity.class;
                Intent intent = new Intent(LgActivity.this, TargetClass);
                startActivity(intent);
            }
        });
    }
}
