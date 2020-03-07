package com.uk.location.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class RegisterDialog {

    private Button btnSubmit, btn_privacy, btn_abort;
    private SeekBar sb_age;
    private TextView tv_age;
    private EditText et_loginAccount, et_loginPassword;
    private Context context;
    private Spinner sp_occupation, sp_edu;
    private String[] sp_item_job = {"服务业", "农民/工人", "医务人员", "退休人员", "其他"};
    private String[] sp_item_education = {"小学或以下", "初中", "高中", "大学", "硕士", "博士或其他"};
    private Dialog privacyDialog;

    public RegisterDialog(Context context) {
        init(context);
    }



    public void init(final Context context) {
        final Dialog registerDialog = new Dialog(context);
        registerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before

        registerDialog.setContentView(R.layout.dialog_register);
        registerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        registerDialog.setCanceledOnTouchOutside(false);
        Window window = registerDialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
        registerDialog.show();


        tv_age = registerDialog.findViewById(R.id.tv_age);
        btnSubmit = registerDialog.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDialog.dismiss();

                //to do
                //check all information required and toast
            }
        });

        btn_privacy = registerDialog.findViewById(R.id.btn_privacy);
        btn_privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privacyDialog = new Dialog(context);
                privacyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
                privacyDialog.setContentView(R.layout.dialog_privacy);
                privacyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button btn_privacy_return = privacyDialog.findViewById(R.id.btn_return);
                btn_privacy_return.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        privacyDialog.dismiss();
                    }
                });
                privacyDialog.setCanceledOnTouchOutside(false);
                Window window = privacyDialog.getWindow();
                window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                privacyDialog.show();
            }
        });

        btn_abort = registerDialog.findViewById(R.id.btn_Tnc3);
        btn_abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDialog.dismiss();
            }
        });

        et_loginAccount = registerDialog.findViewById(R.id.et_loginAccount);
        et_loginPassword = registerDialog.findViewById(R.id.et_loginPassword);

        sb_age = registerDialog.findViewById(R.id.sb_age);
        sb_age.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_age.setText("" + progress + "岁");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sp_occupation = registerDialog.findViewById(R.id.sp_occupation3);
        ArrayAdapter aa_occupation = new ArrayAdapter(context, android.R.layout.simple_spinner_item, sp_item_job);
        aa_occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_occupation.setAdapter(aa_occupation);

        sp_edu = registerDialog.findViewById(R.id.sp_edu2);
        ArrayAdapter aa_edu = new ArrayAdapter(context, android.R.layout.simple_spinner_item, sp_item_education);
        aa_edu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_edu.setAdapter(aa_edu);


    }

}