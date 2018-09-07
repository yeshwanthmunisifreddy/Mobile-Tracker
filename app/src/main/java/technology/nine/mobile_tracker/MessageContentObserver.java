package technology.nine.mobile_tracker;

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

import technology.nine.mobile_tracker.data.CallLogsDBHelper;

public class MessageContentObserver extends ContentObserver {
    private Context context;
    private  CallLogsDBHelper helper ;
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DATE_SENT = "date_sent";
    public static final String COLUMN_PROTOCOL = "protocol";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_BODY = "body";
    public static final String COLUMN_TYPE = "type";
    public static final String UPDATE_SMS_LOGS_UI = "UpdateSmsLogsUi";

    public MessageContentObserver(Context context,CallLogsDBHelper helper) {
        super(new Handler());
        this.context = context;
        this.helper = helper;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Log.e("OnChange","is called");
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    insertSmsDatabase();
                }
            }, 2000);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            helper.close();
        }

    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

    }
    private void updateUi() {
        LocalBroadcastManager.getInstance(context)
                .sendBroadcast(new Intent(UPDATE_SMS_LOGS_UI));

    }

    private void insertSmsDatabase() {
        try {
            Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, " date DESC LIMIT  1 ");
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        String body = cursor.getString(cursor.getColumnIndex(COLUMN_BODY)); //content of sms
                        String add = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)); //phone num
                        long date = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE)); //date
                        String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                        String  messageType = null;
                        if (type .equals("1")){
                            messageType = "Sent";
                        }if (type.equals("2"))
                        {
                            messageType = "Received";
                        }
                        Date d1 = new Date(date);
                            DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

                        DateFormat timeFormat = new SimpleDateFormat(" hh:mm a ");
                        String startDate = dateFormat.format(d1);
                        String startTime = timeFormat.format(d1);
                        if (!helper.readSMSLogs(add,body, startDate,startTime)){
                          if ( helper.insertSMS(add,body,startTime,startDate,messageType)){
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
}

