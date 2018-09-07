package technology.nine.mobile_tracker;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import technology.nine.mobile_tracker.data.CallLogsDBHelper;
import technology.nine.mobile_tracker.service.CallDetectService;
import technology.nine.mobile_tracker.service.SmsDetectingService;

public class MessageContentObserver extends ContentObserver {
    private Context context;
    private  CallLogsDBHelper helper ;
    public MessageContentObserver(Context context,CallLogsDBHelper helper) {
        super(new Handler());
        this.context = context;
        this.helper = helper;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
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

    private void insertSmsDatabase() {
        try {


            Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, " date DESC LIMIT  1 ");
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        String body = cursor.getString(cursor.getColumnIndex("body")); //content of sms
                        String add = cursor.getString(cursor.getColumnIndex("address")); //phone num
                        long date = cursor.getLong(cursor.getColumnIndex("date")); //date
                        String type = cursor.getString(cursor.getColumnIndex("type"));
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
                        Log.e("Message",body +" "+add + " "+ startDate +" "+ startTime + type + " "+ messageType);
                        helper.insertSMS(add,body,startTime,startDate,messageType);
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

