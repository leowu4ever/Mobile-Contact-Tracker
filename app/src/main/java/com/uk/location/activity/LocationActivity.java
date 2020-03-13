package com.uk.location.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.UiSettings;

import java.io.File;

public class LocationActivity extends Activity {

    public static BaiduMap baiduMap = null;
    public static TextView tvTest = null;
    public static Button btnUpload;
    private Button btnReport, btnViewReport, btnLocate, btnLogout;
    private MapView mapView = null;
    private MyBroadcastReceiver broadcastReceiver;

    private LocationHelper lh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        broadcastReceiver = new MyBroadcastReceiver();
        lh = new LocationHelper(this);
        initMap();
        initUIs();
        lh.getLocation(false);
    }

    private void initUIs() {
        btnReport = findViewById(R.id.btn_report);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecordEntryDialog(LocationActivity.this);

            }
        });

        btnUpload = findViewById(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LocationHelper.getTrackingState()) {
                    btnUpload.setText("开启后台定位采集");
                    btnUpload.setBackgroundResource(R.drawable.buttonshape);
                    broadcastReceiver.stopAlarm(getApplicationContext());
                } else {
                    btnUpload.setText("关闭后台定位采集");
                    btnUpload.setBackgroundResource(R.drawable.buttonshape_red);
                    broadcastReceiver.startAlarm(getApplicationContext());
                }
            }
        });

        btnViewReport = findViewById(R.id.btn_viewReport);
        btnViewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RecordHistoryDialog(LocationActivity.this);
            }
        });

        btnLocate = findViewById(R.id.btn_locate);
        btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lh.getLocation(false);
            }
        });

        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                startActivity(intent);
                File file = new File(Environment.getExternalStorageDirectory() + "/VirTrack/userdetails");

                if (file.delete()) {
                    System.out.println("File deleted successfully");
                } else {
                    System.out.println("Failed to delete the file");
                }

                Toast.makeText(getApplicationContext(), "退登成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMap() {
        mapView = findViewById(R.id.bmapView);
        mapView.showZoomControls(false);
        mapView.showScaleControl(false);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        UiSettings uiSetting = baiduMap.getUiSettings();
        uiSetting.setZoomGesturesEnabled(false);
        uiSetting.setOverlookingGesturesEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onBackPressed() {
        
    }
}