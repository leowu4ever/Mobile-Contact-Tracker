package com.uk.location.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uk.location.activity.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class RecordActivity extends Activity {
    private Button btn_submit;
    private SeekBar sb_age, sb_duration, sb_members;
    private TextView tv_age, tv_duration, tv_members;
    private EditText et_job, et_area;
    private DatePicker dp_date;
    private RadioGroup rg_gender;
    private Spinner sp_edu;
    private String[] sp_item_education = {"小学或以下", "初中", "高中", "大学"};
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
                dataCollection();
            }
        });

        et_job = findViewById(R.id.et_job);
        et_area = findViewById(R.id.et_area);
//        dp_date = findViewById(R.id.dp_date);
//        dp_date.setMaxDate(System.currentTimeMillis());

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

    private void dataCollection() {
        int age = sb_age.getProgress();
        String job = et_job.getText().toString();
        String edu = sp_edu.getSelectedItem().toString();
        String area = et_area.getText().toString();
        DecimalFormat dateMonthFormatter = new DecimalFormat("00");
        DecimalFormat yearFormatter = new DecimalFormat("0000");
        String date = dateMonthFormatter.format(dp_date.getDayOfMonth()) + "-" + dateMonthFormatter.format(dp_date.getMonth()+1) + "-" + yearFormatter.format(dp_date.getYear());
        String dateForFile = dateMonthFormatter.format(dp_date.getDayOfMonth()) + dateMonthFormatter.format(dp_date.getMonth()+1) + yearFormatter.format(dp_date.getYear());

        int duration = sb_duration.getProgress();
        int members = sb_members.getProgress();
        Log.d("FUNCTION", "EVAEVAEVA");
        if (gender != null && !(edu.equals("")) && !(area.equals(""))) {
            RecordManager rcm = new RecordManager();
            Gson gson = new Gson();
            rcm.createRecord(gender, age, job, edu, area, date, duration, members);

            String json = gson.toJson(rcm);
            Log.d("JSON", json);

            DateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
            String sysDate = formatter.format(Calendar.getInstance().getTime());
            new File(this.getFilesDir(), "record_" + sysDate);

            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("record_" + dateForFile + sysDate , Context.MODE_PRIVATE));
                outputStreamWriter.write(json);
                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }

            finish();

        } else {
            Log.d("JSON", "json");
            Toast t = Toast.makeText(getApplicationContext(), "請填寫所有欄位", Toast.LENGTH_LONG);
            t.show();
        }
    }

}
