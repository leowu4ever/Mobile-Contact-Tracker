package com.uk.location.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;


public class RecordDetailsDialog {

    private Record record;
    private TextView tv_name, tv_gender, tv_age, tv_job, tv_edu, tv_date, tv_area, tv_duration, tv_members;
    private Button btn_close;
    public RecordDetailsDialog(Context context, String data) {
        init(context, data);
    }

    public void init(final Context context, final String rawData) {

        final Dialog recordDetailsDialog = new Dialog(context);
        recordDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        recordDetailsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        recordDetailsDialog.setContentView(R.layout.dialog_past_record_view);
        recordDetailsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        recordDetailsDialog.setCanceledOnTouchOutside(false);
        Window window = recordDetailsDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        recordDetailsDialog.show();

        Gson gson = new Gson();
        Record record = gson.fromJson(rawData, Record.class);

        tv_gender = recordDetailsDialog.findViewById(R.id.tv_pr_gender);
        tv_gender.setText(record.getGender());

        tv_age = recordDetailsDialog.findViewById(R.id.tv_pr_age);
        tv_age.setText(record.getAge() + "岁");

        tv_job = recordDetailsDialog.findViewById(R.id.tv_pr_job);
        tv_job.setText(record.getJob());

        tv_edu = recordDetailsDialog.findViewById(R.id.tv_pr_edu);
        tv_edu.setText(record.getEdu());

        tv_date = recordDetailsDialog.findViewById(R.id.tv_pr_date);
        tv_date.setText(record.getDate());

        tv_area = recordDetailsDialog.findViewById(R.id.tv_pr_area);
        tv_area.setText(record.getArea());

        tv_duration = recordDetailsDialog.findViewById(R.id.tv_pr_duration);
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

        tv_members = recordDetailsDialog.findViewById(R.id.tv_pr_members);
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

        btn_close = recordDetailsDialog.findViewById(R.id.btn_contact_detail_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordDetailsDialog.dismiss();
            }
        });
    }

    public void setData(Record rc) {
        record = rc;
    }
}
