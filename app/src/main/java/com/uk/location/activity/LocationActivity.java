package com.uk.location.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;


public class LocationActivity extends Activity {

    public static final int LOCATION_UPLOAD_INTERVAL = 1 * 1000 * 60 * 10; //10min
    public static Runnable countdownRunnbale;
    private Button btnReport, btnViewReport, btnUpload, btnLocate;
    private MapView mapView = null;
    private BaiduMap baiduMap = null;
    private LocationClient locationClient = null;
    private LocationClient uploadClient = null;
    private NotificationUtils mNotificationUtils;
    private Notification notification;
    private boolean isEnableLocInForeground = false;
    private BDAbstractLocationListener locationListener = null;
    private BDAbstractLocationListener uploadListener = null;
    private String DEBUG_TAG = "loa1";
    private int countDown = 0;
    private TextView tvLocationHelper = null;
    private Dialog recordListDialog;


    private TextView tvTest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(DEBUG_TAG, "create");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        tvTest = findViewById(R.id.tv_test);
        tvTest.append("start" + "\n");

        initMap();
        initLocationClient();
        initUploadClient();
        initNotification();
        initUIs();

        locationClient.start();

    }

    private void initUIs() {
        tvLocationHelper = findViewById(R.id.tv_location_helper);
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
                if (isEnableLocInForeground) {
                    Log.d(DEBUG_TAG, "endback");
                    btnUpload.setText("开启后台定位采集");

                    uploadClient.disableLocInForeground(true);
                    isEnableLocInForeground = false;
                    uploadClient.unRegisterLocationListener(uploadListener);
                    uploadClient.stop();
                    btnUpload.setBackgroundResource(R.drawable.buttonshape);

                } else {
                    //开启后台定位
                    Log.d(DEBUG_TAG, "startback");
                    btnUpload.setText("关闭后台定位采集");

                    uploadClient.enableLocInForeground(1001, notification);// 调起前台定位
                    isEnableLocInForeground = true;
                    uploadClient.registerLocationListener(uploadListener);
                    uploadClient.start();
                    btnUpload.setBackgroundResource(R.drawable.buttonshape_red);

                    LocationActivity.this.moveTaskToBack(true);
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
                locationClient.stop();
                locationClient.start();
            }
        });
    }

    private void initNotification() {
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationUtils = new NotificationUtils(this);
            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("疫迹 - 你也可以为扼制疫情发展出一份力", "已开启后台定位采集");
            notification = builder2.build();
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(LocationActivity.this);
            Intent nfIntent = new Intent(LocationActivity.this, LocationActivity.class);

            builder.setContentIntent(PendingIntent.
                    getActivity(LocationActivity.this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("疫迹 - 你也可以为扼制疫情发展出一份力") // 设置下拉列表里的标题
                    .setSmallIcon(R.drawable.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("已开启后台定位采集") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
            notification = builder.build(); // 获取构建好的Notification
        }
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
    }

    private void initUploadClient() {
        uploadClient = new LocationClient(this);

        LocationClientOption uploadOption = new LocationClientOption();
        uploadOption.setOpenGps(true);
        uploadOption.setScanSpan(LOCATION_UPLOAD_INTERVAL);
        uploadOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        uploadOption.setCoorType("bd09ll");

        uploadListener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.d(DEBUG_TAG, "upload client" + latitude + "," + longitude);
                    Calendar currentTime = Calendar.getInstance();
                    tvTest.append("("+currentTime.get(Calendar.HOUR) + " : "
                            + currentTime.get(Calendar.MINUTE)+  " : "
                            + currentTime.get(Calendar.SECOND)+  ")  "
                            + latitude + " " + longitude + "\n");
                } else {
                    Log.d(DEBUG_TAG, "Cant find your location");
                }
            }
        };
        uploadClient.setLocOption(uploadOption);
    }

    private void initLocationClient() {
        locationClient = new LocationClient(this);

        LocationClientOption locationOption = new LocationClientOption();
        locationOption.setOpenGps(true);
        //locationOption.setScanSpan(0);
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        locationOption.setCoorType("bd09ll");

        locationListener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Log.d(DEBUG_TAG, "location client" + latitude + "," + longitude);
                    setPosition2Center(baiduMap, location, true);
                    locationClient.stop();

                } else {
                    Log.d(DEBUG_TAG, "Cant find your location");
                }
            }
        };
        locationClient.setLocOption(locationOption);
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
        locationClient.registerLocationListener(locationListener);
        Log.d(DEBUG_TAG, "resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        Log.d(DEBUG_TAG, "pause");
        locationClient.stop();
        locationClient.unRegisterLocationListener(locationListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        uploadClient.disableLocInForeground(true);
        isEnableLocInForeground = false;
        uploadClient.unRegisterLocationListener(uploadListener);
        uploadClient.stop();
        Log.d(DEBUG_TAG, "destory");
    }

    public void setPosition2Center(BaiduMap map, BDLocation bdLocation, Boolean isShowLoc) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(bdLocation.getRadius()).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        map.setMyLocationData(locData);

        if (isShowLoc) {
            LatLng ll = new LatLng(bdLocation.getLatitude() - 0.005, bdLocation.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(16);
            map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        }
    }

    @Override
    public void onBackPressed() {
    }
}