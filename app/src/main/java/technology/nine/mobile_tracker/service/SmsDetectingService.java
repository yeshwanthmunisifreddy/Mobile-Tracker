package technology.nine.mobile_tracker.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.util.Log;

import technology.nine.mobile_tracker.MessageContentObserver;
import technology.nine.mobile_tracker.data.CallLogsDBHelper;

public class SmsDetectingService extends Service {
    CallLogsDBHelper helper = new CallLogsDBHelper(SmsDetectingService.this);

    public SmsDetectingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.getApplicationContext().getContentResolver().registerContentObserver(Telephony.Sms.CONTENT_URI, true, new MessageContentObserver(this, helper));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        getContentResolver().unregisterContentObserver(new MessageContentObserver(getApplicationContext(), helper));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("SMS Destroy", "is called");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("technology.nine.mobile_tracker.SMS_SERVICE");
        sendBroadcast(broadcastIntent);
        getContentResolver().unregisterContentObserver(new MessageContentObserver(getApplicationContext(), helper));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
