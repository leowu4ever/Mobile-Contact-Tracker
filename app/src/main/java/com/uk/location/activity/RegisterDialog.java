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
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterDialog {

    private Button btnSubmit, btn_privacy, btn_abort;
    private SeekBar sb_age;
    private TextView tvAge;
    private EditText etLoginAccount, etLoginPassword;
    private Spinner spOccupation, spEdu, spGender;
    private String[] sp_item_gender = {"请选择", "男", "女"};
    private String[] sp_item_occupation = {"请选择", "服务业", "农民/工人", "医务人员", "退休人员", "其他"};
    private String[] sp_item_education = {"请选择", "小学或以下", "初中", "高中", "大学", "硕士", "博士或其他"};
    private Dialog privacyDialog;
    private SpinnerHelper spinnerHelper;
    private JsonFileHelper jsonFileHelper;

    public RegisterDialog(Context context) {
        init(context);
    }


    public void init(final Context context) {
        final Dialog registerDialog = new Dialog(context);
        registerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before

        spinnerHelper = new SpinnerHelper();

        registerDialog.setContentView(R.layout.dialog_register);
        etLoginAccount = registerDialog.findViewById(R.id.et_loginaccount);
        etLoginPassword = registerDialog.findViewById(R.id.et_loginpassword);

        new DialogHelper().displayDialog(registerDialog);

        btnSubmit = registerDialog.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spOccupation.getSelectedItemPosition() != 0 && spEdu.getSelectedItemPosition() != 0 && spGender.getSelectedItemPosition() != 0 && etLoginAccount.getText() != null && etLoginPassword.getText() != null) {
                    jsonFileHelper = new JsonFileHelper(etLoginAccount.getText().toString());
                    jsonFileHelper.saveRegistrationDataFromLocal(new RegistrationData(etLoginAccount.getText().toString(), etLoginPassword.getText().toString()));
                    registerDialog.dismiss();
                    Toast.makeText(context, "已注册成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "请填写全部信息", Toast.LENGTH_SHORT).show();
                }
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

        btn_abort = registerDialog.findViewById(R.id.btn_registerdismiss);
        btn_abort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerDialog.dismiss();
            }
        });

        tvAge = registerDialog.findViewById(R.id.tv_spinnerage);

        sb_age = registerDialog.findViewById(R.id.sb_age);
        sb_age.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvAge.setText("" + progress + "岁");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        spGender = registerDialog.findViewById(R.id.sp_gender);
        ArrayAdapter aa_gender = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_gender) {
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_gender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGender.setAdapter(aa_gender);

        spOccupation = registerDialog.findViewById(R.id.sp_occupation);
        ArrayAdapter aa_occupation = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_occupation) {
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_occupation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spOccupation.setAdapter(aa_occupation);

        spEdu = registerDialog.findViewById(R.id.sp_edu);
        ArrayAdapter aa_edu = new ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item, sp_item_education) {
            @Override
            public boolean isEnabled(int position) {
                return spinnerHelper.disableDefaultItem(position);
            }
        };
        aa_edu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEdu.setAdapter(aa_edu);
    }
}