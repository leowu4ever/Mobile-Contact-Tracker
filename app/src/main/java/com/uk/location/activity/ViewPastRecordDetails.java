package com.uk.location.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;


public class ViewPastRecordDetails extends Activity {

    private Record record;
    private TextView tv_name, tv_gender, tv_age, tv_job, tv_edu, tv_date, tv_area, tv_duration, tv_members;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String rawdata = getIntent().getStringExtra("PASTDATA");
        Gson gson = new Gson();
        Record record = gson.fromJson(rawdata, Record.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_record_view);

        tv_gender = findViewById(R.id.tv_pr_gender);
        tv_gender.setText(record.getGender());

        tv_age = findViewById(R.id.tv_pr_age);
        tv_age.setText(record.getAge() + "岁");

        tv_job = findViewById(R.id.tv_pr_job);
        tv_job.setText(record.getJob());

        tv_edu = findViewById(R.id.tv_pr_edu);
        tv_edu.setText(record.getEdu());

        tv_date = findViewById(R.id.tv_pr_date);
        tv_date.setText(record.getDate());

        tv_area = findViewById(R.id.tv_pr_area);
        tv_area.setText(record.getArea());

        tv_duration = findViewById(R.id.tv_pr_duration);
        String duration = "";
        int durationFromData = record.getDuration();
        if (durationFromData == 0) {
            duration = "30分钟或以下";
        } else if (durationFromData > 0 && durationFromData < 19) {
            duration = (durationFromData + 1) / 2 + "小时";
            if ((durationFromData + 1) % 2 == 1) {
                duration += "30分钟";
            }
        } else if (durationFromData == 19) {
            duration = "10小时或以上";
        }
        tv_duration.setText(duration);

        tv_members = findViewById(R.id.tv_pr_members);
        String membercount = "";
        int memberFromData = record.getMembers();
        if (memberFromData == 0) {
            membercount = "无";
        } else if (memberFromData >= 1 && memberFromData <= 9) {
            membercount = memberFromData + "名";
        } else if (memberFromData >= 10) {
            membercount = memberFromData + "名或以上";
        }
        tv_members.setText(membercount);
    }

    public void setData(Record rc) {
        record = rc;
    }
}
