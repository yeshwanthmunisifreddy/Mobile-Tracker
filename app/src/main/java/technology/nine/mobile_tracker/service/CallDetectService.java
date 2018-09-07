package technology.nine.mobile_tracker.service;


import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.util.Log;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import technology.nine.mobile_tracker.data.CallLogsDBHelper;
import technology.nine.mobile_tracker.model.CallLogs;

import static android.provider.Telephony.Sms.Inbox.CONTENT_URI;

public class CallDetectService extends Service {
    public static final String UPDATE_CALL_LOGS_UI = "UpdateCallLogsUi";
    CallLogsDBHelper helper = new CallLogsDBHelper(CallDetectService.this);
    public CallDetectService() {

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

    public void updateUi() {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(new Intent(UPDATE_CALL_LOGS_UI));

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

}