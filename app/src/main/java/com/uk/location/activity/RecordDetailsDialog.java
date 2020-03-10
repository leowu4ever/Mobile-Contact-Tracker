package com.uk.location.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;


public class RecordDetailsDialog {

    private Record record;
    private TextView tv_name, tv_gender, tv_age, tv_job, tv_edu, tv_date, tv_time, tv_area, tv_duration, tv_members;
    private Button btn_close;
    public RecordDetailsDialog(Context context, String data) {
        init(context, data);
    }

    public void init(final Context context, final String rawData) {

        final Dialog recordDetailsDialog = new Dialog(context);
        recordDetailsDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        recordDetailsDialog.setContentView(R.layout.dialog_record_detail);

        new DialogHelper().displayDialog(recordDetailsDialog);

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

        tv_time = recordDetailsDialog.findViewById(R.id.tv_pr_time);
        tv_time.setText(record.getTime());

        tv_area = recordDetailsDialog.findViewById(R.id.tv_pr_area);
        tv_area.setText(record.getArea());

        tv_duration = recordDetailsDialog.findViewById(R.id.tv_pr_duration);
        tv_duration.setText(record.getDuration());

        tv_members = recordDetailsDialog.findViewById(R.id.tv_pr_members);
        tv_members.setText(record.getMembers());

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
