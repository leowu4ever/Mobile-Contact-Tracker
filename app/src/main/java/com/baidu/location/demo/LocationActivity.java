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

	private MapView mapView = null;
	private BaiduMap baiduMap = null;
	private LocationClient locationClient = null;

	private NotificationUtils mNotificationUtils;
	private Notification notification;

	private boolean isEnableLocInForeground = false;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("locationAc", "start");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loca);

		mapView = findViewById(R.id.bmapView);
		mapView.showZoomControls(false);
		mapView.showScaleControl(false);

		baiduMap = mapView.getMap();
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(5));
		baiduMap.setMyLocationEnabled(true);

		locationClient = new LocationClient(this);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setScanSpan(2000);
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");

		BDAbstractLocationListener listener = new BDAbstractLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub

				if(location != null) {
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();

					Log.d("locationAc",  Double.toString(latitude) + "," + Double.toString(longitude));
					setPosition2Center(baiduMap, location, true);

				} else {
					Log.d("locationAc", "Cant find your location");
				}
			}
		};

		locationClient.registerLocationListener(listener);
		locationClient.setLocOption(option);
		locationClient.start();


		if (Build.VERSION.SDK_INT >= 26) {
			mNotificationUtils = new NotificationUtils(this);
			Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
					("适配android 8限制后台定位功能", "正在后台定位");
			notification = builder2.build();
		} else {
			//获取一个Notification构造器
			Notification.Builder builder = new Notification.Builder(LocationActivity.this);
			Intent nfIntent = new Intent(LocationActivity.this, LocationActivity.class);

			builder.setContentIntent(PendingIntent.
					getActivity(LocationActivity.this, 0, nfIntent, 0)) // 设置PendingIntent
					.setContentTitle("适配android 8限制后台定位功能") // 设置下拉列表里的标题
					.setSmallIcon(R.drawable.ic_launcher) // 设置状态栏内的小图标
					.setContentText("正在后台定位") // 设置上下文内容
					.setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
			notification = builder.build(); // 获取构建好的Notification
		}
		notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音


		btnReport = findViewById(R.id.btn_report);
		btnReport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

//				Class<?> TargetClass = RecordActivity.class;
//				Intent intent = new Intent(LocationActivity.this, TargetClass);
//				startActivity(intent);

				if(isEnableLocInForeground){
					//关闭后台定位（true：通知栏消失；false：通知栏可手动划除）
					Log.d("locationAc", "endback");
					locationClient.disableLocInForeground(true);
					isEnableLocInForeground = false;
					locationClient.stop();
				} else {
					//开启后台定位
					Log.d("locationAc", "startback");
					locationClient.enableLocInForeground(1001, notification);// 调起前台定位
					isEnableLocInForeground = true;
					locationClient.start();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时必须调用mMapView. onResume ()
		mapView.onResume();
		locationClient.start();
		Log.d("locationAc", "resume");

//		locationClient.disableLocInForeground(true);
//		locationClient.stop();


	}

	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时必须调用mMapView. onPause ()
		mapView.onPause();
//		locationClient.stop();
		Log.d("locationAc", "stop");

//		locationClient.enableLocInForeground(1, notification);
//		locationClient.start();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时必须调用mMapView.onDestroy()
		mapView.onDestroy();
//		locationClient.stop();
		Log.d("locationAc", "destory");

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

