package com.uk.location.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.UiSettings;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class LocationActivity extends Activity {

    public static BaiduMap baiduMap = null;
    public static TextView tvTest = null;
    public static Button btnUpload;
    private Button btnReport, btnViewReport, btnLocate, btnLogout;
    private MapView mapView = null;
    private TrackingAlarmReceiver broadcastReceiver;
    private LocationHelper locationHelper;
    private JsonFileHelper jsonFileHelper;
    private String currentUser, token, cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        currentUser = getIntent().getStringExtra("USER_ID");
        token = getIntent().getStringExtra("USER_TOKEN");
        cookie = getIntent().getStringExtra("COOKIE");
        jsonFileHelper = new JsonFileHelper(currentUser);
        broadcastReceiver = new TrackingAlarmReceiver();
        locationHelper = new LocationHelper(currentUser, token, this);
        MainActivity.getLocationPermission(this);
        initMap();
        initUIs();
        locationHelper.getLocation(false);
    }

    private void initUIs() {
        btnReport = findViewById(R.id.btn_report);
        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Record_webView();
                Intent intent = new Intent(LocationActivity.this, Record_webView.class);
                intent.putExtra("USER_ID", currentUser);
                intent.putExtra("USER_TOKEN", token);
                intent.putExtra("URL_SUFFIX", "contactHistory.jsp");
                intent.putExtra("COOKIE", cookie);
                startActivity(intent);
            }
        });

        // update tracking state since tracking process won't stop even reboot.
        RegistrationData registrationData = jsonFileHelper.readRegistrationDataFromLocal();
        btnUpload = findViewById(R.id.btn_upload);
        boolean trackingState = registrationData.getTrackingState();
        if (trackingState) {
            btnUpload.setText("关闭后台定位采集");
            btnUpload.setBackgroundResource(R.drawable.buttonshape_red);
            broadcastReceiver.startAlarm(currentUser, token, getApplicationContext());
        } else {
            btnUpload.setText("开启后台定位采集");
            btnUpload.setBackgroundResource(R.drawable.buttonshape);
            broadcastReceiver.stopAlarm(currentUser, token, getApplicationContext());
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
                    broadcastReceiver.stopAlarm(currentUser, token, getApplicationContext());

                    registrationData.setTrackingState(false);
                    jsonFileHelper.saveRegistrationDataFromLocal(registrationData);

                } else {
                    btnUpload.setText("关闭后台定位采集");
                    btnUpload.setBackgroundResource(R.drawable.buttonshape_red);
                    broadcastReceiver.startAlarm(currentUser, token, getApplicationContext());

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
                new Record_webView();
                Intent intent = new Intent(LocationActivity.this, Record_webView.class);
                intent.putExtra("USER_ID", currentUser);
                intent.putExtra("USER_TOKEN", token);
                intent.putExtra("URL_SUFFIX", "dailyActivity.jsp");
                intent.putExtra("COOKIE", cookie);
                startActivity(intent);
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
                new LogoutDialog(LocationActivity.this, currentUser, token, broadcastReceiver);
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