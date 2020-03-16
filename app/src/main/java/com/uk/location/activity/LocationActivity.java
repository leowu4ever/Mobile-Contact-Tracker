package com.uk.location.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
    private TrackingAlarmReceiver broadcastReceiver;

    private LocationHelper locationHelper;
    private JsonFileHelper jsonFileHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        jsonFileHelper = new JsonFileHelper();
        broadcastReceiver = new TrackingAlarmReceiver();
        locationHelper = new LocationHelper(this);
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

        // update tracking state since tracking process won't stop even reboot.
        RegistrationData registrationData = jsonFileHelper.readRegistrationDataFromLocal();
        btnUpload = findViewById(R.id.btn_upload);
        boolean trackingState = registrationData.getTrackingState();
        if (trackingState) {
            btnUpload.setText("关闭后台定位采集");
            btnUpload.setBackgroundResource(R.drawable.buttonshape_red);
        } else {
            btnUpload.setText("开启后台定位采集");
            btnUpload.setBackgroundResource(R.drawable.buttonshape);
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegistrationData registrationData = jsonFileHelper.readRegistrationDataFromLocal();
                boolean trackingState = registrationData.getTrackingState();

                if (trackingState) {
                    // turn off tracking
                    btnUpload.setText("开启后台定位采集");
                    btnUpload.setBackgroundResource(R.drawable.buttonshape);
                    broadcastReceiver.stopAlarm(getApplicationContext());

                    registrationData.setTrackingState(false);
                    jsonFileHelper.saveRegistrationDataFromLocal(registrationData);

                } else {
                    btnUpload.setText("关闭后台定位采集");
                    btnUpload.setBackgroundResource(R.drawable.buttonshape_red);
                    broadcastReceiver.startAlarm(getApplicationContext());

                    // update tracking state since tracking process won't stop even reboot.
                    registrationData.setTrackingState(true);
                    jsonFileHelper.saveRegistrationDataFromLocal(registrationData);
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
                Intent intent = new Intent(LocationActivity.this, MainActivity.class);
                startActivity(intent);
                File file = new File(Environment.getExternalStorageDirectory() + "/疫迹/userdata.json");

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