package com.baidu.location.demo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.baidu.baidulocationdemo.R;
import com.google.gson.Gson;


public class ViewPastRecordDetails extends Activity {

    private RecordManager rcm;
    private TextView tv_name, tv_gender, tv_age, tv_job, tv_edu, tv_date, tv_area, tv_duration, tv_members;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String rawdata = getIntent().getStringExtra("PASTDATA");
        Gson gson = new Gson();
        RecordManager rcm = gson.fromJson(rawdata, RecordManager.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_record_view);
        tv_name = findViewById(R.id.tv_pr_name);
        tv_name.setText(rcm.getName());

        tv_gender = findViewById(R.id.tv_pr_gender);
        tv_gender.setText(rcm.getGender());

        tv_age = findViewById(R.id.tv_pr_age);
        tv_age.setText(rcm.getAge() + "歲");

        tv_job = findViewById(R.id.tv_pr_job);
        tv_job.setText(rcm.getJob());

        tv_edu = findViewById(R.id.tv_pr_edu);
        tv_edu.setText(rcm.getEdu());

        tv_date = findViewById(R.id.tv_pr_date);
        tv_date.setText(rcm.getDate());

        tv_area = findViewById(R.id.tv_pr_area);
        tv_area.setText(rcm.getArea());

        tv_duration = findViewById(R.id.tv_pr_duration);
        String duration = "";
        int durationFromData = rcm.getDuration();
        if (durationFromData == 0) {
            duration = "30分鐘或以下";
        } else if (durationFromData > 0 && durationFromData < 19) {
            duration = (durationFromData + 1) / 2 + "小時";
            if ((durationFromData + 1) % 2 == 1) {
                duration += "30分鐘";
            }
        } else if (durationFromData == 19) {
            duration = "10小時或以上";
        }
        tv_duration.setText(duration);

        tv_members = findViewById(R.id.tv_pr_members);
        String membercount = "";
        int memberFromData = rcm.getMembers();
        if (memberFromData == 0) {
            membercount = "無";
        } else if (memberFromData >= 1 && memberFromData <= 9) {
            membercount = memberFromData + "名";
        } else if (memberFromData >= 10) {
            membercount = memberFromData + "名或以上";
        }
        tv_members.setText(membercount);
    }

    public void setData(RecordManager rc) {
        rcm = rc;
    }
}
