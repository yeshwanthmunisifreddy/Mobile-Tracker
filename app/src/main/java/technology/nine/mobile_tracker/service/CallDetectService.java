package technology.nine.mobile_tracker.service;


import android.Manifest;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.CallLog;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsManager;
import android.util.Log;

import java.text.DateFormat;

import technology.nine.mobile_tracker.data.CallLogsDBHelper;

import static android.provider.Telephony.Sms.Inbox.CONTENT_URI;

public class CallDetectService extends Service {
    public static final String UPDATE_UI = "UpdateUi";

    public CallDetectService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int res = super.onStartCommand(intent, flags, startId);
        CallLogsDBHelper helper = new CallLogsDBHelper(CallDetectService.this);
        if (intent != null) {
            String phoneNumber = intent.getStringExtra("PhoneNumber");
            String startDate = intent.getStringExtra("StartDate");
            String startTime = intent.getStringExtra("StartTime");
            String duration = intent.getStringExtra("Duration");
            String callType = intent.getStringExtra("CallType");
            // ContentResolver resolver = getContentResolver()
            Cursor cursor = null;

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                try {
                    cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1");
                    // cursor =getContentResolver().query(CONTENT_URI,null,null,null,null);
                    if (cursor != null) {
                        try {
                            while (cursor.moveToNext()) {
                                String name = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
                                String number = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
                                String durations = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.DURATION));
                                String callTypes = cursor.getString(cursor.getColumnIndex(CallLog.Calls.TYPE));
                                // Log.e("CallLogs", name + " " + number + " " + callTypes + " " + startDate + " " + startTime + " "+ durations);
                                if (helper.insertCallLog(name, number, callTypes, startDate, startTime, durations)) {

                                    updateUi();
                                }
                            }
                        } catch (Exception e) {
                        e.printStackTrace();
                        } finally {
                            cursor.close();
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    helper.close();
                }
            }


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
        //  Log.e("UpdateUi","is called");
        Intent intent = new Intent(UPDATE_UI);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(intent);

    }

}
