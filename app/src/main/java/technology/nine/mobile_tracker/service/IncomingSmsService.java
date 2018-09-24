package technology.nine.mobile_tracker.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import technology.nine.mobile_tracker.data.LogsDBHelper;
import technology.nine.mobile_tracker.utils.MessageContentObserver;

public class IncomingSmsService extends Service {
    private static final String UPDATE_SMS_LOGS_UI = "UpdateSmsLogsUi";
    LogsDBHelper helper = new LogsDBHelper(IncomingSmsService.this);

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= 26) {
            startMyOwnForeground();
        }

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.e("onStartCommand", "is called in IncomingSMSService");
        int res = super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        readDate(intent);
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void readDate(Intent intent) {
        String number = intent.getStringExtra("Number");
        String body = intent.getStringExtra("MessageBody");
        String date = intent.getStringExtra("Date");
        String time = intent.getStringExtra("Time");
        String type = "Received";
        if (!helper.readSMSLogs(number, body, date, time)) {
            if (helper.insertSMS(number, body, time, date, type)) {
                updateUi();
                Log.e("Incoming", "is called");
            }
        }
    }

    private void updateUi() {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(new Intent(UPDATE_SMS_LOGS_UI));
        stopSelf();

    }

    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "technology.nine.Mobile_tracker";
        String channelName = " SMS Detect Service ";
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
                    .build();
        }
        startForeground(2, notification);
    }
}
