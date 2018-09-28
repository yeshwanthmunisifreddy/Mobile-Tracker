package technology.nine.mobile_tracker.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import technology.nine.mobile_tracker.data.LogsDBHelper;
import technology.nine.mobile_tracker.utils.InsertSms;
import technology.nine.mobile_tracker.utils.Util;

import static technology.nine.mobile_tracker.utils.MessageContentObserver.UPDATE_SMS_LOGS_UI;
//Read sent sms in android Nougat and above
public class SmsJobServices extends JobService {
    private LogsDBHelper helper = new LogsDBHelper(SmsJobServices.this);

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.e("OnStartJob", "is called");
        try {
            insertSms(params);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            helper.close();
            Util.scheduleJob(getApplicationContext());
        }

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.e("onStopJob", "is called");
        return false;
    }

    private void insertSms(JobParameters params) {
        Log.e("insertSms", "is called ");
        boolean inserted = InsertSms.insertSMS(getApplicationContext());
        Log.e("Inserted", inserted + "");
        if (inserted) {
            updateUi();
        }
        jobFinished(params, false);
    }

    private void updateUi() {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(new Intent(UPDATE_SMS_LOGS_UI));

    }
}
