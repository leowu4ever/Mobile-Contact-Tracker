package com.uk.location.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;


public class LogoutDialog{

    private Button btn_close, btn_logout;
    private TrackingAlarmReceiver broadcastReceiver;
    private String currentUser, token;

    public LogoutDialog(Context context, String currentUser, String token, TrackingAlarmReceiver broadcastReceiver) {
        this.broadcastReceiver = broadcastReceiver;
        this.currentUser = currentUser;
        this.token = token;
        init(context);
    }

    public void init(final Context context) {

        final Dialog logoutDialog = new Dialog(context);
        logoutDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        logoutDialog.setContentView(R.layout.dialog_logout);

        new DialogHelper().displayDialog(logoutDialog);

        btn_close = logoutDialog.findViewById(R.id.btn_logoutCancel);
        btn_close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logoutDialog.dismiss();
            }
        });

        btn_logout = logoutDialog.findViewById(R.id.btn_logoutConfirm);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                logoutDialog.dismiss();
                broadcastReceiver.stopAlarm(currentUser, token, context);
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                File file = new File(Environment.getExternalStorageDirectory() + "/疫迹/autologin_userdetails.json");

                if (file.delete()) {
                    System.out.println("File deleted successfully");
                } else {
                    System.out.println("Failed to delete the file");
                }

                Toast.makeText(context, "退登成功", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
