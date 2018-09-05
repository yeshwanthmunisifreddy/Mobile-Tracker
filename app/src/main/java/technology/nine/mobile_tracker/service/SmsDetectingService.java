package technology.nine.mobile_tracker.service;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.util.Log;
import android.os.Process;
import technology.nine.mobile_tracker.MessageContentObserver;

public class SmsDetectingService extends Service {
    public static final String serviceIntent = "custom.SMS_SERVICE";
    private Handler handler = new Handler();
    public SmsDetectingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.getApplicationContext().getContentResolver().registerContentObserver(Telephony.Sms.CONTENT_URI, true, new MessageContentObserver(this));

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SMS onStartCommand", "is called");
        super.onStartCommand(intent, flags, startId);

        return START_STICKY;
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("SMS TaskRemoved", "is Called");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("technology.nine.mobile_tracker.SMS_SERVICE");
        sendBroadcast(broadcastIntent);
        getContentResolver().unregisterContentObserver(new MessageContentObserver(getApplicationContext()));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SMS Destroy", "is called");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("technology.nine.mobile_tracker.SMS_SERVICE");
        sendBroadcast(broadcastIntent);
        getContentResolver().unregisterContentObserver(new MessageContentObserver(getApplicationContext()));

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
