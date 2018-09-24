package technology.nine.mobile_tracker.service;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.data.LogsDBHelper;

public class CallDetectService extends Service {
    public static final String UPDATE_CALL_LOGS_UI = "UpdateCallLogsUi";
    LogsDBHelper helper = new LogsDBHelper(CallDetectService.this);

    public CallDetectService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26){
            startMyOwnForeground();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int res = super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            final String startDate = intent.getStringExtra("StartDate");
            final String startTime = intent.getStringExtra("StartTime");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        readDate(startDate, startTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        helper.close();
                    }
                }
            }, 2000);
        }
        return res;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void readDate(String startDate, String startTime) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
            Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1");
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        String name = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
                        String number = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                        String durations = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION));
                        String callTypes = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                        if (helper.insertCallLog(name, number, callTypes, startDate, startTime, durations)) {
                            updateUi();
                        }
                    }
                } finally {
                    cursor.close();
                }

            }

        }

    }

    public void updateUi() {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(new Intent(UPDATE_CALL_LOGS_UI));
        stopSelf();

    }


    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "technology.nine.Mobile_tracker";
        String channelName = " Call Detect Service";
        NotificationChannel chan = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan.setLightColor(Color.BLUE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        }
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(chan);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            notification = notificationBuilder.setOngoing(true)
                    .setContentTitle("App is running in background")
                    .setPriority(NotificationManager.IMPORTANCE_MIN)
                    .setCategory(Notification.CATEGORY_SERVICE)
                    .setAutoCancel(true)
                    .build();
        }
        startForeground(2, notification);
    }

}