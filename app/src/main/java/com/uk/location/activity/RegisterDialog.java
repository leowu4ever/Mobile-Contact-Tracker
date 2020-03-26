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
    private EditText etLoginAccount, etLoginPassword, etLoginPasswordConfirm;

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
        etLoginPasswordConfirm = registerDialog.findViewById(R.id.et_loginpasswordconfirm);

        new DialogHelper().displayDialog(registerDialog);

        btnSubmit = registerDialog.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etLoginAccount.getText() != null && etLoginPassword.getText() != null && etLoginPasswordConfirm.getText() != null) {
                    if (etLoginPassword.getText().toString().equals(etLoginPasswordConfirm.getText().toString())) {
                        String data = "{\"username\":\"" + etLoginAccount.getText().toString() + "\",\"password\":\"" + etLoginPassword.getText().toString() + "\"}";
                        NetworkHelper nh = new NetworkHelper();
                        String returnText = "";
                        try {
                            returnText = nh.CallAPI("POST", "authentication/login", data, "").get();
                        } catch (Exception e) {
                            returnText = "Error";
                        }
                        if (returnText.equals("Error: incorrect password")) {
                            Toast.makeText(context, "账户已存在，请重试", Toast.LENGTH_SHORT).show();
                        }
                        else if (returnText.contains("Error")){
                            Toast.makeText(context, "注册失败, 请重试", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            jsonFileHelper = new JsonFileHelper(etLoginAccount.getText().toString());
                            jsonFileHelper.saveRegistrationDataFromLocal(new RegistrationData(etLoginAccount.getText().toString(), etLoginPassword.getText().toString()));
                            registerDialog.dismiss();
                            Toast.makeText(context, "注册成功", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "密码不符合要求 请重新输入", Toast.LENGTH_SHORT).show();
                    }
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
    }
}