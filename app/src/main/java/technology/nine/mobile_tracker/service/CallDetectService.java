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
    CallLogsDBHelper helper = new CallLogsDBHelper(CallDetectService.this);

    public CallDetectService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int res = super.onStartCommand(intent, flags, startId);
         insertData(intent);

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

    private void insertData(Intent intent) {
        if (intent != null) {
            String phoneNumber = intent.getStringExtra("PhoneNumber");
            String startDate = intent.getStringExtra("StartDate");
            String startTime = intent.getStringExtra("StartTime");
            String duration = intent.getStringExtra("Duration");
            String callType = intent.getStringExtra("CallType");
            try {
                Log.e("CallDetectService",phoneNumber + startDate+startTime+duration+callType+"");
                if (helper.insertCallLog(" ", phoneNumber, callType, startDate, startTime, duration)) {
                    updateUi();
                }
            } finally {
                helper.close();
            }
        }
    }
}
