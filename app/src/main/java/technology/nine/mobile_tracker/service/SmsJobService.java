package technology.nine.mobile_tracker.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.database.Cursor;
import android.provider.Telephony;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import technology.nine.mobile_tracker.data.LogsDBHelper;

import static technology.nine.mobile_tracker.utils.MessageContentObserver.COLUMN_ADDRESS;
import static technology.nine.mobile_tracker.utils.MessageContentObserver.COLUMN_BODY;
import static technology.nine.mobile_tracker.utils.MessageContentObserver.COLUMN_DATE;
import static technology.nine.mobile_tracker.utils.MessageContentObserver.COLUMN_TYPE;
import static technology.nine.mobile_tracker.utils.MessageContentObserver.UPDATE_SMS_LOGS_UI;

public class SmsJobService extends JobService {
    LogsDBHelper helper = new LogsDBHelper(SmsJobService.this);
    public SmsJobService() {
    }
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("OnStartJob", "is called");
        new Thread(new Runnable() {
            @Override
            public void run() {
                readSmsLogs();
            }
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    public void readSmsLogs() {
        try {
            Cursor cursor = getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, " date DESC LIMIT  1 ");
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        String body = cursor.getString(cursor.getColumnIndex(COLUMN_BODY)); //content of sms
                        String add = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)); //phone num
                        long date = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE)); //date
                        String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));//sms type
                        String messageType = null;
                        if (type.equals("1")) {
                            messageType = "Sent";
                        }
                        if (type.equals("2")) {
                            messageType = "Received";
                        }
                        Date d1 = new Date(date);
                        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                        DateFormat timeFormat = new SimpleDateFormat(" hh:mm a ");
                        String startDate = dateFormat.format(d1);
                        String startTime = timeFormat.format(d1);
                        if (!helper.readSMSLogs(add, body, startDate, startTime)) {
                            if (helper.insertSMS(add, body, startTime, startDate, messageType)) {
                                Log.e("UpdateFunction","is called");
                                updateUi();
                            }
                        }

                    }
                } finally {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUi() {
        Log.e("Update","is called");
        LocalBroadcastManager.getInstance(this)
                .sendBroadcast(new Intent(UPDATE_SMS_LOGS_UI));

    }
}

