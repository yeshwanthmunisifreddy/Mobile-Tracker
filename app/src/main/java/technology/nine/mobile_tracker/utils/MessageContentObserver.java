package technology.nine.mobile_tracker.utils;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.Telephony;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import technology.nine.mobile_tracker.data.LogsDBHelper;

public class MessageContentObserver extends ContentObserver {
    private Context context;
    private LogsDBHelper helper;
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_TYPE = "type";
    public static final String UPDATE_SMS_LOGS_UI = "UpdateSmsLogsUi";

    public MessageContentObserver(Context context, LogsDBHelper helper) {
        super(new Handler());
        this.context = context;
        this.helper = helper;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.e("OnChange", "is called");
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    insertSms();
                }
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }

    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

    }

    private void insertSms() {
        boolean inserted = InsertSms.insertSMS(context);
        if (inserted){
            updateUi();
        }
    }

    private void updateUi() {
        LocalBroadcastManager.getInstance(context)
                .sendBroadcast(new Intent(UPDATE_SMS_LOGS_UI));

    }

}

