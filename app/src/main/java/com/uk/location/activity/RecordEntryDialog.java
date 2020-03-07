package com.uk.location.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RecordEntryDialog {
    private Button btn_submit;
    private SeekBar sb_age, sb_duration, sb_members;
    private TextView tv_age, tv_duration, tv_members;
    private EditText et_area, et_date, et_time;
    private RadioGroup rg_gender;
    private Spinner sp_edu, sp_occupation;
    private String[] sp_item_education = {"小学或以下", "初中", "高中", "大学", "硕士", "博士或其他"};
    private String[] sp_item_job = {"服务业", "农民/工人", "医务人员", "退休人员", "其他"};
    private String[] sp_item_contact_location = {"家中", "通勤过程", "工作场合", "餐厅", "学校", "医院", "商场", "他人家中", "公园", "市场","其他"};
    private String[] sp_item_month = {}; //1-12 月
    private String[] sp_item_day = {}; //1-31日
    private String[] sp_item_hour = {}; // 1-24时
    private String[] sp_item_min = {}; //0-59分
    private String[] sp_item_gender = {"男", "女"};
    private String[] sp_item_age = {}; //0-100
    private String[] sp_item_family_size = {"1", "2", "3", "4", "5", "6", "7", "8", "其他"};
    private String[] sp_item_time_spent = {"0.5小时", "1小时"}; //0.5-10小时，补全
    private String gender;

    public RecordEntryDialog(Context context) {
        init(context);
    }

    public void init(final Context context) {
        gender = "男";
        final Dialog recordDialog = new Dialog(context);
        recordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        recordDialog.setContentView(R.layout.dialog_record);
        recordDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        recordDialog.setCanceledOnTouchOutside(false);
        Window window = recordDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        recordDialog.show();

        btn_submit = recordDialog.findViewById(R.id.btn_RecordSubmit);
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
                if (gender != null && !(edu.equals("")) && !(area.equals(""))) { //TODO: INPUT Validation, Replace "/" in file name
                    Record record = new Record();
                    record.createRecord(gender, age, job, edu, area, date, duration, members);
                    record.objectToFile(record, dateForFile);
                    Toast.makeText(context, "已保存接触人员信息，谢谢。", Toast.LENGTH_LONG).show();
                    recordDialog.dismiss();
                } else {
                    Toast.makeText(context, "请填写全部信息", Toast.LENGTH_LONG).show();
                }
            }
        });

        et_area = recordDialog.findViewById(R.id.et_area);
        et_date = recordDialog.findViewById(R.id.et_date);

        sb_age = recordDialog.findViewById(R.id.sb_age);
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

        sb_duration = recordDialog.findViewById(R.id.sb_duration);
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

        sb_members = recordDialog.findViewById(R.id.sb_members);
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

        sp_edu = recordDialog.findViewById(R.id.sp_edu);
        ArrayAdapter aa_edu = new ArrayAdapter(context, android.R.layout.simple_spinner_item, sp_item_education);
        aa_edu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_edu.setAdapter(aa_edu);

        sp_occupation = recordDialog.findViewById(R.id.sp_occupation);
        ArrayAdapter aa_job = new ArrayAdapter(context, android.R.layout.simple_spinner_item, sp_item_job);
        aa_job.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_occupation.setAdapter(aa_job);

        rg_gender = recordDialog.findViewById(R.id.rg_gender);
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

        tv_age = recordDialog.findViewById(R.id.tv_age);
        tv_duration = recordDialog.findViewById(R.id.tv_duration);
        tv_members = recordDialog.findViewById(R.id.tv_members);

    }
}
