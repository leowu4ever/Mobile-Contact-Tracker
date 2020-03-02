package com.baidu.location.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.baidulocationdemo.R;
import com.google.gson.Gson;


public class RecordActivity extends Activity {
    private Button btn_submit;
    private SeekBar sb_age, sb_duration, sb_members;
    private TextView tv_age, tv_duration, tv_members;
    private Spinner sp_edu;
    private String[] sp_item_education = {"小學或以下", "初中", "高中", "大學"};
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataCollection();
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

        sb_duration = findViewById(R.id.sb_duration);
        sb_duration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = "";
                if (progress==0){
                    text = "30分鐘或以下";
                }else if(progress>0 && progress<19) {
                    text = (progress + 1) / 2 + "小時";
                    if ((progress + 1) % 2 == 1) {
                        text += "30分鐘";
                    }
                }else if(progress==19){
                    text = "10小時或以上";
                }

                tv_duration.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_members = findViewById(R.id.sb_members);
        sb_members.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = "";
                if (progress == 0 ){
                    text = "無";
                }else if (progress>=1&&progress<=9){
                    text = progress+"名";
                }else if (progress>=10){
                    text = progress + "名或以上";
                }
                tv_members.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sp_edu = findViewById(R.id.sp_edu);
        ArrayAdapter aa_edu = new ArrayAdapter(this,android.R.layout.simple_spinner_item,sp_item_education);
        aa_edu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_edu.setAdapter(aa_edu);

        tv_age = findViewById(R.id.tv_age);
        tv_duration = findViewById(R.id.tv_duration);
        tv_members = findViewById(R.id.tv_members);


    }
    private void dataCollection(){

    }


}
