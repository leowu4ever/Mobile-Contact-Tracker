package com.uk.location.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RecordActivity extends Activity {
    private Button btn_submit;
    private SeekBar sb_age, sb_duration, sb_members;
    private TextView tv_age, tv_duration, tv_members;
    private EditText et_area, et_date, et_time;
    private RadioGroup rg_gender;
    private Spinner sp_edu, sp_occupation;
    private String[] sp_item_education = {"小学或以下", "初中", "高中", "大学", "硕士", "博士或其他"};
    private String[] sp_item_job = {"服务业", "农民/工人", "医务人员", "退休人员", "其他"};
    private String gender;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record);

        gender = "男";

        btn_submit = findViewById(R.id.btn_RecordSubmit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int age = sb_age.getProgress();
                String job = sp_occupation.getSelectedItem().toString();
                String edu = sp_edu.getSelectedItem().toString();
                String area = et_area.getText().toString();
                String date = et_date.getText().toString();
                String dateForFile = date.replace("-", "");
                int duration = sb_duration.getProgress();
                int members = sb_members.getProgress();
                if (gender != null && !(edu.equals("")) && !(area.equals("")) ) { //TODO: INPUT Validation, Replace "/" in file name
                    Record record = new Record();
                    record.createRecord(gender, age, job, edu, area, date, duration, members);
                    record.objectToFile(record, dateForFile);
                    Toast.makeText(getApplicationContext(), "已储存记录", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "請填寫所有欄位", Toast.LENGTH_LONG).show();
                }
            }
        });

        et_area = findViewById(R.id.et_area);
        et_date = findViewById(R.id.et_date);

        sb_age = findViewById(R.id.sb_age);
        sb_age.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_age.setText(progress + " 岁");
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
                if (progress == 0) {
                    text = "30分钟以内";
                } else if (progress > 0 && progress < 19) {
                    text = (progress + 1) / 2 + "小时";
                    if ((progress + 1) % 2 == 1) {
                        text += "30分钟";
                    }
                } else if (progress == 19) {
                    text = "10小时以上";
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
                if (progress >= 0 && progress <= 9) {
                    progress = progress + 1;
                    text = progress + "名";
                } else if (progress >= 10) {
                    text = progress + "名以上";
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
        ArrayAdapter aa_edu = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sp_item_education);
        aa_edu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_edu.setAdapter(aa_edu);

        sp_occupation = findViewById(R.id.sp_occupation);
        ArrayAdapter aa_job = new ArrayAdapter(this, android.R.layout.simple_spinner_item, sp_item_job);
        aa_job.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_occupation.setAdapter(aa_job);

        rg_gender = findViewById(R.id.rg_gender);
        rg_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (rg_gender.indexOfChild(rg_gender.findViewById(checkedId))) {
                    case 0:
                        gender = "男";
                        break;
                    case 1:
                        gender = "女";
                        break;
                }
            }
        });

        tv_age = findViewById(R.id.tv_age);
        tv_duration = findViewById(R.id.tv_duration);
        tv_members = findViewById(R.id.tv_members);


    }
}
