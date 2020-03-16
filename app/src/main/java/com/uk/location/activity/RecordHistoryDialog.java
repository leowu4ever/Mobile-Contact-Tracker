package com.uk.location.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecordHistoryDialog {

    private Button btnUploadHistory, btnDismissHisory;
    private String currentUser;

    public RecordHistoryDialog(String currentID, Context context) {
        currentUser = currentID;
        init(context);
    }


    public void init(final Context context) {
        final Dialog recordListDialog = new Dialog(context);
        recordListDialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        recordListDialog.setContentView(R.layout.dialog_record_history);

        new DialogHelper().displayDialog(recordListDialog);

        File rootDircectory = new File(Environment.getExternalStorageDirectory() + "/疫迹/" + currentUser + "/");
        String[] files = rootDircectory.list();

        LinearLayout parentLayout = recordListDialog.findViewById(R.id.layout_RecordView);
        parentLayout.removeAllViews();

        List<String> colList = new ArrayList(Arrays.asList(files));
        List<String> workableList = new ArrayList();
        for (String str : colList) {
            if (str.contains("_location_log")) {
                workableList.add(str);
            }
        }

        Collections.sort(workableList);

        if (workableList.size() != 0) {
            String date = "";
            int seq = 0;

            for (String str : workableList) {
                LinearLayout recordContainer = new LinearLayout(context);
                Button btnTag = new Button(context);
                TextView tvRecord = new TextView(context);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT
                );

                recordContainer.setLayoutParams(params);
                recordContainer.setWeightSum(1);
                recordContainer.setGravity(Gravity.CENTER_VERTICAL);
                recordContainer.setPadding(10, 2, 0, 2);

                btnTag.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                if (!(str.substring(7, 11).equals(date))) {
                    date = str.substring(7, 11);
                    seq = 0;
                    TextView tvDateSep = new TextView(context);
                    String dateSeperator = str.substring(0, str.lastIndexOf("_"));
                    dateSeperator = ((dateSeperator.substring(0, dateSeperator.lastIndexOf("_")+1)).replaceFirst("_", "月")).replaceFirst("_", "日");
                    tvDateSep.setText(dateSeperator);
                    tvDateSep.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                    tvDateSep.setTextColor(Color.parseColor("#55578A"));
                    tvDateSep.setTypeface(tvDateSep.getTypeface(), Typeface.BOLD);
                    parentLayout.addView(tvDateSep);

                    View vSep = new View(context);
                    vSep.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    vSep.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                    parentLayout.addView(vSep);
                }
                seq += 1;

                tvRecord.setText("接触记录 - " + seq);
                tvRecord.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
                tvRecord.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvRecord.setTextColor(Color.parseColor("#55578A"));

                btnTag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                btnTag.setText("查看");
                btnTag.setTypeface(btnTag.getTypeface(), Typeface.BOLD);

                btnTag.setTag(str);
                btnTag.setBackgroundResource(R.drawable.buttonround);
                btnTag.setTextColor(context.getResources().getColor(android.R.color.background_light));
                btnTag.setLayoutParams(new LinearLayout.LayoutParams((int) (60 * context.getResources().getDisplayMetrics().density), (int) (30 * context.getResources().getDisplayMetrics().density)));
                btnTag.setGravity(Gravity.CENTER);
                btnTag.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button b = (Button) v;
                        String readings = "";
                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(new File(Environment.getExternalStorageDirectory() + "/疫迹/" + b.getTag().toString()));  // 2nd line
                            InputStreamReader isr = new InputStreamReader(fis);
                            BufferedReader br = new BufferedReader(isr);
                            StringBuilder sb = new StringBuilder();
                            String readinText = "";

                            while ((readinText = br.readLine()) != null) {
                                sb.append(readinText);
                            }
                            readings = sb.toString();
                            new RecordDetailsDialog(context, readings);


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

        } else {
            TextView tvNullMessage = new TextView(context);
            tvNullMessage.setText("没有已上报接触人员");
            tvNullMessage.setLayoutParams(new TableLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
            tvNullMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            tvNullMessage.setGravity(Gravity.CENTER);
            parentLayout.addView(tvNullMessage);
        }

        btnUploadHistory = recordListDialog.findViewById(R.id.btn_upload_history);
        btnUploadHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo
                // upload json here
            }
        });


        btnDismissHisory = recordListDialog.findViewById(R.id.btn_dismiss_historylist);
        btnDismissHisory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordListDialog.dismiss();
            }
        });
    }
}
