package com.uk.location.activity;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.Calendar;

public class LocationHelper {

    public static boolean isTracking = false;
    private LocationClient client;
    private BDAbstractLocationListener listener;
    private String DEBUG_TAG = "loa1";
    private NotificationUtils mNotificationUtils;
    private Notification notification;
    private boolean isForTracking = false;


    public LocationHelper(Context context) {
        initClient(context);
        initNotification(context);
    }

    public static boolean getTrackingState() {
        return isTracking;
    }

    public static void setTrackingState(boolean trackingState) {
        isTracking = trackingState;
    }

    public void setPosition2Center(BaiduMap map, BDLocation bdLocation) {
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(bdLocation.getRadius())
                .direction(bdLocation.getRadius()).latitude(bdLocation.getLatitude())
                .longitude(bdLocation.getLongitude()).build();
        map.setMyLocationData(locData);

        LatLng ll = new LatLng(bdLocation.getLatitude() - 0.005, bdLocation.getLongitude());
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(ll).zoom(16);
        map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
    }

    private void initClient(final Context context) {

        final Context c = context;
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");

        listener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                LocationActivity.tvTest.append(Integer.toString(location.getLocType()) + "\n");
                if (!isForTracking) {
                    Toast.makeText(context, "定位中...", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "后台定位中...", Toast.LENGTH_SHORT).show();
                }
                if (location != null) {
                    if (isForTracking) {
                        // do database here
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.d(DEBUG_TAG, "upload client" + latitude + "," + longitude);
                        Calendar currentTime = Calendar.getInstance();
                        LocationActivity.tvTest.append("(" + currentTime.get(Calendar.HOUR) + " : "
                                + currentTime.get(Calendar.MINUTE) + " : "
                                + currentTime.get(Calendar.SECOND) + ")  "
                                + latitude + " " + longitude + "\n");
                    } else {
                        // move camera
                        setPosition2Center(LocationActivity.baiduMap, location);
                    }
                } else {
                    Toast.makeText(c, "暂时无法定位到你", Toast.LENGTH_SHORT).show();
                }
                stopClientService();
            }
        };

        client = new LocationClient(context);
        client.setLocOption(option);
        client.registerLocationListener(listener);
    }

    public void getLocation(boolean tracking) {
        this.isForTracking = tracking;
        client.start();
    }

    public void stopClientService() {
        client.stop();
    }

    public void stopClientListener () {
        client.unRegisterLocationListener(listener);
    }

    public void enableNoti() {
        client.enableLocInForeground(1001, getNoti());
    }

    public void disableNoti() {
        client.disableLocInForeground(true);
    }

    private void initNotification(Context context) {
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationUtils = new NotificationUtils(context);
            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("疫迹 - 你也可以为扼制疫情发展出一份力", "已开启后台定位采集");
            notification = builder2.build();
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(context);
            Intent nfIntent = new Intent(context, LocationActivity.class);

            builder.setContentIntent(PendingIntent.
                    getActivity(context, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("疫迹 - 你也可以为扼制疫情发展出一份力") // 设置下拉列表里的标题
                    .setSmallIcon(R.drawable.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("已开启后台定位采集") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
            notification = builder.build(); // 获取构建好的Notification
        }
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
    }

    public Notification getNoti() {
        return notification;
    }
}