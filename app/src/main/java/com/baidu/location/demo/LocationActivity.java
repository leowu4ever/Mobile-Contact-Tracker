package com.baidu.location.demo;

import com.baidu.baidulocationdemo.R;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LocationActivity extends Activity {

	private Button btnReport;
	private Button btnUpload;

	private MapView mapView = null;
	private BaiduMap baiduMap = null;
	private LocationClient locationClient = null;
	private LocationClient uploadClient = null;

	private NotificationUtils mNotificationUtils;
	private Notification notification;

	private boolean isEnableLocInForeground = false;
	public static final int LOCATION_UPLOAD_INTERVAL = 1;	//in min

	private BDAbstractLocationListener locationListener = null;
	private BDAbstractLocationListener uploadListener = null;

	private String DEBUG_TAG = "loa1";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(DEBUG_TAG, "create");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loca);
		initMap();
		initLocationClient();
		initUploadClient();
		initNotification();
		initUIs();
	}

	private void initUIs() {
		btnReport = findViewById(R.id.btn_report);
		btnReport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Class<?> TargetClass = RecordActivity.class;
				Intent intent = new Intent(LocationActivity.this, TargetClass);
				startActivity(intent);
			}
		});

		btnUpload = findViewById(R.id.btn_upload);
		btnUpload.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isEnableLocInForeground){
					Log.d(DEBUG_TAG, "endback");
					uploadClient.disableLocInForeground(true);
					isEnableLocInForeground = false;
					btnUpload.setText("关闭后台定位采集");
					uploadClient.unRegisterLocationListener(uploadListener);
					uploadClient.stop();
				} else {
					//开启后台定位
					Log.d(DEBUG_TAG, "startback");
					uploadClient.enableLocInForeground(1001, notification);// 调起前台定位
					isEnableLocInForeground = true;
					btnUpload.setText("关闭后台定位采集");
					uploadClient.registerLocationListener(uploadListener);
					uploadClient.start();
				}
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
		uploadOption.setScanSpan(LOCATION_UPLOAD_INTERVAL * 60 * 1000);
		uploadOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		uploadOption.setCoorType("bd09ll");

		uploadListener = new BDAbstractLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub

				if(location != null) {
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();

					Log.d(DEBUG_TAG,  "upload client" + Double.toString(latitude) + "," + Double.toString(longitude));
					setPosition2Center(baiduMap, location, true);

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
		locationOption.setScanSpan(5000);
		locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		locationOption.setCoorType("bd09ll");

		locationListener = new BDAbstractLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub

				if(location != null) {
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();
					Log.d(DEBUG_TAG,  "location client" + Double.toString(latitude) + "," + Double.toString(longitude));
					setPosition2Center(baiduMap, location, true);

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
		baiduMap.setMyLocationEnabled(true);
	}

	@Override
	protected void onStart() {
		super.onStart();
		locationClient.registerLocationListener(locationListener);
		locationClient.start();
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
		locationClient.stop();
		locationClient.unRegisterLocationListener(locationListener);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		Log.d(DEBUG_TAG, "destory");
	}

	public void setPosition2Center(BaiduMap map, BDLocation bdLocation, Boolean isShowLoc) {
		MyLocationData locData = new MyLocationData.Builder()
				.accuracy(bdLocation.getRadius())
				.direction(bdLocation.getRadius()).latitude(bdLocation.getLatitude())
				.longitude(bdLocation.getLongitude()).build();
		map.setMyLocationData(locData);

		if (isShowLoc) {
			LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
			MapStatus.Builder builder = new MapStatus.Builder();
			builder.target(ll).zoom(18f);
			map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
		}
	}
}

