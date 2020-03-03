package com.baidu.location.demo;

import com.baidu.baidulocationdemo.R;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.service.LocationService;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LocationActivity extends Activity {

	private Button btnReport, btnViewReport;

	private MapView mapView = null;
	private BaiduMap baiduMap = null;
	private LocationClient locationClient = null;

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

		locationClient = new LocationClient(getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		option.setCoorType("bd09ll");

		BDAbstractLocationListener listener = new BDAbstractLocationListener() {
			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub

				if(location != null) {
					double latitude = location.getLatitude();
					double longitude = location.getLongitude();
					String place = location.getCity();
					Log.d("locationAc", Double.toString(latitude) + "," + Double.toString(longitude) + place);
					setPosition2Center(baiduMap, location, true);

				} else {
					Log.d("locationAc", "Cant find your location");
				}

			}
		};

		locationClient.registerLocationListener(listener);
		locationClient.setLocOption(option);
		locationClient.start();

		btnReport = findViewById(R.id.btn_report);
		btnReport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Class<?> TargetClass = RecordActivity.class;
				Intent intent = new Intent(LocationActivity.this, TargetClass);
				startActivity(intent);
			}
		});

		btnViewReport = findViewById(R.id.btn_viewReport);
		btnViewReport.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Class<?> TargetClass = ViewRecordActivity.class;
				Intent intent = new Intent(LocationActivity.this, TargetClass);
				startActivity(intent);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		//在activity执行onResume时必须调用mMapView. onResume ()
		mapView.onResume();
		locationClient.restart();
		Log.d("locationAc", "resume");

	}

	@Override
	protected void onPause() {
		super.onPause();
		//在activity执行onPause时必须调用mMapView. onPause ()
		mapView.onPause();
		locationClient.stop();
		Log.d("locationAc", "stop");

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//在activity执行onDestroy时必须调用mMapView.onDestroy()
		mapView.onDestroy();
		locationClient.stop();
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
			builder.target(ll).zoom(10f);
			map.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
		}
	}
}

