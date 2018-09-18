package technology.nine.mobile_tracker.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import technology.nine.mobile_tracker.data.LogsDBHelper;
import technology.nine.mobile_tracker.utils.MessageContentObserver;

public class IncomingSmsService extends Service {
    private static final String UPDATE_SMS_LOGS_UI = "UpdateSmsLogsUi";
    LogsDBHelper helper = new LogsDBHelper(IncomingSmsService.this);

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
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
    private  void readDate(Intent intent){
        String number = intent.getStringExtra("Number");
        String body = intent.getStringExtra("MessageBody");
        String date= intent.getStringExtra("Date");
        String time = intent.getStringExtra("Time");
        String type = "Received";
        if (!helper.readSMSLogs(number, body, date, time)) {
            if (helper.insertSMS(number, body, time, date, type)) {
                updateUi();
                Log.e("Incoming","is called");
            }

        }

    }
    private void updateUi() {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(new Intent(UPDATE_SMS_LOGS_UI));

    }
}
