package com.uk.location.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends Activity {

    private Button btnLogin, btnRegister;
    private EditText etUserName, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUIs();
    }

    private void initUIs() {
        etUserName = findViewById(R.id.et_Main_userName);
        etPassword = findViewById(R.id.et_Main_Password);

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {//Need to validate not null later
            @Override
            public void onClick(View v) {
                // password check
                String input = "{\"userName\": \"" + etUserName.getText().toString() + "\",\n" + "\"password\": \"" + etPassword.getText().toString() + "\n" + "}";

                DateFormat formatter = new SimpleDateFormat("ddMMyyyyHHmmss");
                String sysDate = formatter.format(Calendar.getInstance().getTime());
                String PATH_LOCAL = Environment.getExternalStorageDirectory() + "/VirTrack/";

                File rootfolder = new File(PATH_LOCAL);
                if (!rootfolder.exists()) {
                    rootfolder.mkdir();
                }

                new File(rootfolder, "userdetails");

                try (FileWriter writer = new FileWriter(PATH_LOCAL + "userdetails")) {
                    writer.write(input);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                startActivity(intent);
            }
        });

        btnRegister = findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RegisterDialog(MainActivity.this);
            }
        });
    }
}
