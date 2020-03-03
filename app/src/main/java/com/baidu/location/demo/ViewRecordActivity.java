package com.baidu.location.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.baidu.baidulocationdemo.R;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ViewRecordActivity extends Activity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_record_list);
        String[] files = this.fileList();
        List colList = new ArrayList(Arrays.asList(files));
        Collections.sort(colList);
        LinearLayout parentLayout = findViewById(R.id.layout_RecordView);
        parentLayout.removeAllViews();
        for (String str : files) {
            if (str.startsWith("record_")) {
                Log.d("FILE", str);
                Log.d("TEXT", str.substring(2,2));
                Button btnTag = new Button(this);
                btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                btnTag.setText(str.substring(21) + " @ " + str.substring(7,9) + "-" + str.substring(9,11) + "-" +str.substring(11,15) + " " + str.substring(15,17) + ":" +str.substring(17,19));
                btnTag.setTag(str);
                btnTag.setBackgroundResource(R.drawable.buttonshape);
                btnTag.setTextColor(getResources().getColor(android.R.color.background_light));
                btnTag.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                btnTag.setGravity(Gravity.CENTER);
                btnTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button)v;

                        try {
                            InputStream inputStream = context.openFileInput(b.getTag().toString());

                            if (inputStream != null) {
                                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                                String receiveString = "";
                                StringBuilder stringBuilder = new StringBuilder();

                                while ((receiveString = bufferedReader.readLine()) != null) {
                                    stringBuilder.append("\n").append(receiveString);
                                }

                                inputStream.close();
                                String ret = stringBuilder.toString();

                                Class<?> TargetClass = ViewPastRecordDetails.class;
                                Intent intent = new Intent(ViewRecordActivity.this, TargetClass);
                                intent.putExtra("PASTDATA", ret);

                                startActivity(intent);
                            }
                        } catch (FileNotFoundException e) {
                            Log.e("login activity", "File not found: " + e.toString());
                        } catch (IOException e) {
                            Log.e("login activity", "Can not read file: " + e.toString());
                        }

                    }
                });
                parentLayout.addView(btnTag);
            }
        }
    }
}