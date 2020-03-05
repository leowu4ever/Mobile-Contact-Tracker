package com.uk.location.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
        File rootDircectory = new File(Environment.getExternalStorageDirectory() + "/VirTrack/");
        String[] files = rootDircectory.list();
        LinearLayout parentLayout = findViewById(R.id.layout_RecordView);
        parentLayout.removeAllViews();
        
        if (files != null) {
        List<String> colList = new ArrayList(Arrays.asList(files));
        Collections.sort(colList);

        String date = "";
        int seq = 0;

            for (String str : colList) {
                if (str.startsWith("record_")) {
                    LinearLayout recordContainer = new LinearLayout(this);
                    Button btnTag = new Button(this);
                    TextView tvRecord = new TextView(this);

                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    );
                    params.setMargins((int) (10 * context.getResources().getDisplayMetrics().density), (int) (8 * context.getResources().getDisplayMetrics().density), 0, 0);

                    recordContainer.setLayoutParams(params);
                    recordContainer.setWeightSum(1);
                    recordContainer.setGravity(Gravity.CENTER_VERTICAL);

                    btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    if (!(str.substring(7, 15).equals(date))) {
                        date = str.substring(7, 15);
                        seq = 0;
                        TextView tvDateSep = new TextView(this);
                        tvDateSep.setText(str.substring(7, 9) + "-" + str.substring(9, 11) + "-" + str.substring(11, 15));
                        tvDateSep.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        tvDateSep.setPadding(0, (int) (8 * context.getResources().getDisplayMetrics().density), 0, (int) (-5 * context.getResources().getDisplayMetrics().density));
                        parentLayout.addView(tvDateSep);

                        View vSep = new View(this);
                        vSep.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                        vSep.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                        parentLayout.addView(vSep);
                    }
                    seq += 1;

                    tvRecord.setText("記錄" + seq);
                    tvRecord.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                    tvRecord.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);

                    btnTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    btnTag.setText("𝑖");
                    btnTag.setTag(str);
                    btnTag.setBackgroundResource(R.drawable.buttonround);
                    btnTag.setTextColor(getResources().getColor(android.R.color.background_light));
                    btnTag.setLayoutParams(new LinearLayout.LayoutParams((int) (30 * context.getResources().getDisplayMetrics().density), (int) (30 * context.getResources().getDisplayMetrics().density)));
                    btnTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    btnTag.setGravity(Gravity.CENTER);
                    btnTag.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b = (Button) v;
                            String readings = "";
                            FileInputStream fis = null;
                            try {
                                fis = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/VirTrack/" + b.getTag().toString()));  // 2nd line
                                InputStreamReader isr = new InputStreamReader(fis);
                                BufferedReader br = new BufferedReader(isr);
                                StringBuilder sb = new StringBuilder();
                                String readinText = "";

                                while ((readinText = br.readLine()) != null) {
                                    sb.append(readinText);
                                }
                                readings = sb.toString();

                                Class<?> TargetClass = ViewPastRecordDetails.class;
                                Intent intent = new Intent(ViewRecordActivity.this, TargetClass);
                                intent.putExtra("PASTDATA", readings);

                                startActivity(intent);

                            } catch (FileNotFoundException e) {
                                Log.e("login activity", "File not found: " + e.toString());
                            } catch (IOException e) {
                                Log.e("login activity", "Can not read file: " + e.toString());
                            } finally {
                                if (fis != null) {
                                    try {
                                        fis.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
                    recordContainer.addView(tvRecord);
                    recordContainer.addView(btnTag);
                    parentLayout.addView(recordContainer);
                }
            }
        }else{
            TextView tvNullMessage = new TextView(this);
            tvNullMessage.setText("没有已上报接触人员");
            tvNullMessage.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            tvNullMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            tvNullMessage.setGravity(Gravity.CENTER);
            parentLayout.addView(tvNullMessage);
        }
    }
}
