package com.uk.location.activity;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RecordEntryDialog {
    private Button btnSubmit, btnDismiss;
    private SeekBar sbAge;
    private TextView tvAge;
    private Spinner spGender, spOccupation, spEdu, spApproachDateMonth, spApproachDateDay, spApproachTimehour, spApproachTimeMinute, spDuration, spLocation, spFamilycount;
    private String[] sp_item_education = {"请选择","小学或以下", "初中", "高中", "大学", "硕士", "博士或其他"};
    private String[] sp_item_occupation = {"请选择","服务业", "农民/工人", "医务人员", "退休人员", "其他"};
    private String[] sp_item_contact_location = {"请选择","家中", "通勤过程", "工作场合", "餐厅", "学校", "医院", "商场", "他人家中", "公园", "市场", "其他"};
    private String[] sp_item_month = {"请选择","1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"};
    private String[] sp_item_day = {"请选择","1日", "2日", "3日", "4日", "5日", "6日", "7日", "8日", "9日", "10日", "11日", "12日", "13日", "14日", "15日", "16日", "17日", "18日", "19日", "20日", "21日", "22日", "23日", "24日", "25日", "26日", "27日", "28日", "29日", "30日", "31日"}; //1-31日
    private String[] sp_item_hour = {"请选择","1时", "2时", "3时", "4时", "5时", "6时", "7时", "8时", "9时", "10时", "11时", "12时", "13时", "14时", "15时", "16时", "17时", "18时", "19时", "20时", "21时", "22时", "23时", "24时"}; // 1-24时
    private String[] sp_item_min = {"请选择","1分","2分","3分","4分","5分","6分","7分","8分","9分","10分","11分","12分","13分","14分","15分","16分","17分","18分","19分","20分","21分","22分","23分","24分","25分","26分","27分","28分","29分","30分","31分","32分","33分","34分","35分","36分","37分","38分","39分","40分","41分","42分","43分","44分","45分","46分","47分","48分","49分","50分","51分","52分","53分","54分","55分","56分","57分","58分","59分"}; //0-59分
    private String[] sp_item_gender = {"请选择","男", "女"};
    private String[] sp_item_family_size = {"请选择","1名", "2名", "3名", "4名", "5名", "6名", "7名", "8名", "其他"};
    private String[] sp_item_time_spent = {"请选择","0.5小时", "1小时", "1.5小时", "2小时", "2.5小时", "3小时", "3.5小时", "4小时", "4.5小时", "5小时", "5.5小时", "6小时", "6.5小时", "7小时", "7.5小时", "8小时", "8.5小时", "9小时", "9.5小时", "10小时或以上"};
    private SpinnerHelper spinnerHelper;

    public RecordEntryDialog(Context context) {
        init(context);
    }

    public void init(final Context context) {
        spinnerHelper = new SpinnerHelper();
        final Dialog recordDialog = new Dialog(context);
        recordDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        recordDialog.setContentView(R.layout.dialog_record);

        new DialogHelper().displayDialog(recordDialog);

        tvAge = recordDialog.findViewById(R.id.tv_spinnerage);

        btnSubmit = recordDialog.findViewById(R.id.btn_record_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (spGender.getSelectedItemPosition()!=0 && spOccupation.getSelectedItemPosition()!=0 && spEdu.getSelectedItemPosition()!=0 &&
                        spApproachDateMonth.getSelectedItemPosition()!=0 && spApproachDateDay.getSelectedItemPosition()!=0 &&
                        spApproachTimehour.getSelectedItemPosition()!=0 && spApproachTimeMinute.getSelectedItemPosition()!=0 && spDuration.getSelectedItemPosition()!=0 &&
                        spLocation.getSelectedItemPosition()!=0 && spFamilycount.getSelectedItemPosition()!=0) {
                    int age = sbAge.getProgress();
                    String gender = spGender.getSelectedItem().toString();
                    String job = spOccupation.getSelectedItem().toString();
                    String edu = spEdu.getSelectedItem().toString();
                    String area = spLocation.getSelectedItem().toString();
                    String date = spApproachDateMonth.getSelectedItem().toString() + spApproachDateDay.getSelectedItem().toString();
                    String time = spApproachTimehour.getSelectedItem().toString() + spApproachTimeMinute.getSelectedItem().toString();
                    String duration = spDuration.getSelectedItem().toString();
                    String members = spFamilycount.getSelectedItem().toString();

                    String dateForFilename = String.format("%02d",Integer.parseInt(spApproachDateMonth.getSelectedItem().toString().replaceAll("[^\\d.]", "")))+String.format("%02d",Integer.parseInt(spApproachDateDay.getSelectedItem().toString().replaceAll("[^\\d.]", "")));
                    Record record = new Record();
                    record.createRecord(gender, age, job, edu, area, date, time, duration, members);
                    record.objectToFile(record, dateForFilename);
                    Toast.makeText(context, "已保存接触人员信息，谢谢。", Toast.LENGTH_LONG).show();
                    recordDialog.dismiss();
                } else {
                    Toast.makeText(context, "请填写全部信息", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDismiss = recordDialog.findViewById(R.id.btn_record_dismiss);
        btnDismiss.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                recordDialog.dismiss();
            }
        });

        sbAge = recordDialog.findViewById(R.id.sb_age);
        sbAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAge.setText(progress + " 岁");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        spDuration = recordDialog.findViewById(R.id.sp_duration);

        spFamilycount = recordDialog.findViewById(R.id.sp_familycount);

        sbAge = recordDialog.findViewById(R.id.sb_age);
        sbAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String text = "0歲";
                if (progress >= 0 && progress <= 99) {
                    progress = progress + 1;
                    text = progress + "歲";
                } else if (progress >= 100) {
                    text = progress + "歲或以上";
                }
                tvAge.setText(text);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        spGender = recordDialog.findViewById(R.id.sp_gender);
        ArrayAdapter aa_gender = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_gender){
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(aa_gender);

        spEdu = recordDialog.findViewById(R.id.sp_edu);
        ArrayAdapter aa_edu = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_education){
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_edu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEdu.setAdapter(aa_edu);

        spOccupation = recordDialog.findViewById(R.id.sp_occupation);
        ArrayAdapter aa_job = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_occupation){
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_job.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOccupation.setAdapter(aa_job);

        spApproachDateMonth = recordDialog.findViewById(R.id.sp_approach_datemonth);
        ArrayAdapter aa_datemonth = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_month){
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_datemonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spApproachDateMonth.setAdapter(aa_datemonth);

        spApproachDateDay = recordDialog.findViewById(R.id.sp_approach_dateday);
        ArrayAdapter aa_dateday = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_day){
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_dateday.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spApproachDateDay.setAdapter(aa_dateday);

        spApproachTimehour = recordDialog.findViewById(R.id.sp_approach_timehour);
        ArrayAdapter aa_timehour = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_hour){
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_timehour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spApproachTimehour.setAdapter(aa_timehour);

        spApproachTimeMinute = recordDialog.findViewById(R.id.sp_approach_timeminute);
        ArrayAdapter aa_timeminute = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_min){
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_timeminute.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spApproachTimeMinute.setAdapter(aa_timeminute);

        spDuration = recordDialog.findViewById(R.id.sp_duration);
        ArrayAdapter aa_duration = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_time_spent){
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_duration.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDuration.setAdapter(aa_duration);

        spLocation = recordDialog.findViewById(R.id.sp_area);
        ArrayAdapter aa_location = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_contact_location){
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_location.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLocation.setAdapter(aa_location);

        spFamilycount = recordDialog.findViewById(R.id.sp_familycount);
        ArrayAdapter aa_family_size = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_family_size){
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_family_size.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFamilycount.setAdapter(aa_family_size);
    }


}
