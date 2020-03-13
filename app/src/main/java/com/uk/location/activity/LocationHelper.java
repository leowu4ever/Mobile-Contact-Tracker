package com.uk.location.activity;
import android.content.Context;
import android.location.Location;
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
    private boolean forTracking = false;
    public JsonFileHelper jsonFileHelper;


    public LocationHelper(Context context) {
        initClient(context);
        jsonFileHelper = new JsonFileHelper();
    }

    public static boolean getTrackingState() {
        return isTracking;
    }

    public static void setTrackingState(boolean trackingState) {
        isTracking = trackingState;
    }

    public void zoomMapTo(BaiduMap map, BDLocation bdLocation) {
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

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");

        listener = new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                Calendar currentTime = Calendar.getInstance();
                if (location != null) {
                    if(forTracking) {


                        // write location log
                        LocationLogData locationLogData = jsonFileHelper.readLocationLogDataFromLocal();
                        String date = (currentTime.get(Calendar.MONTH)+1) + "-" + currentTime.get(Calendar.DATE);
                        String time = currentTime.get(Calendar.HOUR_OF_DAY) + ":" + currentTime.get(Calendar.MINUTE) + ":" + currentTime.get(Calendar.SECOND);
                        int errorcode = location.getLocType();
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        locationLogData.addLocationLogData(date, time, latitude, longitude, errorcode);
                        jsonFileHelper.saveLocationLogDataToLocal(locationLogData);

                        String prompt = time + " - "+ latitude + "," + longitude + " - "+  errorcode;
                        Log.d(DEBUG_TAG, prompt);
                        Toast.makeText(context, prompt, Toast.LENGTH_SHORT).show();


                    } else {
                        zoomMapTo(LocationActivity.baiduMap, location);
                    }

                } else {
                    Toast.makeText(context, "定位失败", Toast.LENGTH_SHORT).show();
                }
                stopClientService();
            }
        };

        client = new LocationClient(context);
        client.setLocOption(option);
        client.registerLocationListener(listener);
    }

    public void getLocation(boolean forTracking) {
        this.forTracking = forTracking;
        client.start();
    }

    public void stopClientService() {
        client.stop();
    }
}