package com.uk.location.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
    public static LocationHelper locationHelper;
    private Button btnReport, btnViewReport, btnUpload, btnLocate, btnLogout;
    private MapView mapView = null;
    private String DEBUG_TAG = "loa1";
    private MyBroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationHelper = new LocationHelper(this);

        tvTest = findViewById(R.id.tv_test);
        broadcastReceiver = new MyBroadcastReceiver();

        initMap();
        initUIs();

        locationHelper.getLocation(false);
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
                    locationHelper.disableNoti();
                    btnUpload.setText("开启后台定位采集");
                    btnUpload.setBackgroundResource(R.drawable.buttonshape);

                    // start alarm
                    broadcastReceiver.stopAlarm(getApplicationContext());
                } else {
                    locationHelper.enableNoti();
                    btnUpload.setText("关闭后台定位采集");
                    btnUpload.setBackgroundResource(R.drawable.buttonshape_red);
                    LocationActivity.this.moveTaskToBack(true);

                    // stop alarm
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

                locationHelper.getLocation(false);
            }
        });

        btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo
                //to do adding a double check dialog
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
        //baiduMap.setMyLocationEnabled(true);
        UiSettings uiSetting = baiduMap.getUiSettings();
        uiSetting.setZoomGesturesEnabled(false);
        uiSetting.setOverlookingGesturesEnabled(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(DEBUG_TAG, "start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        Log.d(DEBUG_TAG, "resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        Log.d(DEBUG_TAG, "pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

        locationHelper.stopClientListener();
        locationHelper.disableNoti();
        broadcastReceiver.stopAlarm(getApplicationContext());

        Log.d(DEBUG_TAG, "destory");
    }

    @Override
    public void onBackPressed() {
        LocationActivity.this.moveTaskToBack(true);
    }
}