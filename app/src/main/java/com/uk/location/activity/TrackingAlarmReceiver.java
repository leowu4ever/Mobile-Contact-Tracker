package com.uk.location.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.util.Log;

public class TrackingAlarmReceiver extends BroadcastReceiver {

    private final String ALARM_ACTION_LABEL = "LOCATION_ALARM";
    private AlarmManager am;
    private Intent intent;
    private PendingIntent pi;
    private final int LOCATION_UPLOAD_INTERVAL = 1000 * 1 * 60;
    private PowerManager pm;
    public static LocationHelper locationHelper;


    public void onReceive(Context context, Intent intent) {
        String currentID = intent.getStringExtra("USER_ID");
        locationHelper = new LocationHelper(currentID, context);
        locationHelper.getLocation(true);
        startAlarm(currentID, context);
    }

    public void startAlarm(String currentUser, Context context) {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, TrackingAlarmReceiver.class);
        intent.putExtra("USER_ID", currentUser);
        intent.setAction(ALARM_ACTION_LABEL);
        pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pi);
        AlarmManager.AlarmClockInfo ac = new AlarmManager.AlarmClockInfo(System.currentTimeMillis()+LOCATION_UPLOAD_INTERVAL, pi);
        am.setAlarmClock(ac, pi);
        LocationHelper.setTrackingState(true);
        Log.d("loa1", "alarm starts");
    }

    public void stopAlarm(String currentUser, Context context) {
        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        intent = new Intent(context, TrackingAlarmReceiver.class);
        intent.putExtra("USER_ID", currentUser);
        intent.setAction(ALARM_ACTION_LABEL);
        pi = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.cancel(pi);
        pi.cancel();

        LocationHelper.setTrackingState(false);
        Log.d("loa1", "alarm stops");
    }
}
