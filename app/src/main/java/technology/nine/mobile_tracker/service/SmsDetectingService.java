package technology.nine.mobile_tracker.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.util.Log;

import technology.nine.mobile_tracker.MessageContentObserver;

public class SmsDetectingService extends Service {
    public SmsDetectingService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SMS onStartCommand", "is called");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.getApplicationContext().getContentResolver().registerContentObserver(Telephony.Sms.CONTENT_URI, true, new MessageContentObserver(this));

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("SMS TaskRemoved", "is Called");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("technology.nine.mobile_tracker.SMS_SERVICE");
        sendBroadcast(broadcastIntent);
        getContentResolver().unregisterContentObserver(new MessageContentObserver(getApplicationContext()));
        super.onTaskRemoved(rootIntent);


    }

    @Override
    public void onDestroy() {
        Log.e("SMS Destroy", "is called");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("technology.nine.mobile_tracker.SMS_SERVICE");
        sendBroadcast(broadcastIntent);
        getContentResolver().unregisterContentObserver(new MessageContentObserver(getApplicationContext()));
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
