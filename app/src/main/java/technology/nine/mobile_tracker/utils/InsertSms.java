package technology.nine.mobile_tracker.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.Telephony;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


import technology.nine.mobile_tracker.data.LogsDBHelper;


import static technology.nine.mobile_tracker.utils.MessageContentObserver.COLUMN_ADDRESS;
import static technology.nine.mobile_tracker.utils.MessageContentObserver.COLUMN_BODY;
import static technology.nine.mobile_tracker.utils.MessageContentObserver.COLUMN_DATE;
import static technology.nine.mobile_tracker.utils.MessageContentObserver.COLUMN_TYPE;

//insert sent sms into database
public class InsertSms {
    public static boolean insertSMS(Context context) {
        LogsDBHelper helper = new LogsDBHelper(context);
        boolean inserted = false;
        try {
            Cursor cursor = context.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, " date DESC LIMIT  1 ");
            if (cursor != null) {
                try {
                    while (cursor.moveToNext()) {
                        String body = cursor.getString(cursor.getColumnIndex(COLUMN_BODY)); //content of sms
                        String add = cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)); //phone num
                        Log.e("Body", body + "");
                        long date = cursor.getLong(cursor.getColumnIndex(COLUMN_DATE)); //date
                        String type = cursor.getString(cursor.getColumnIndex(COLUMN_TYPE));
                        String messageType = null;
                        if (type.equals("2")) {
                            messageType = "Sent";
                        }
                        if (type.equals("1")) {
                            messageType = "Received";
                        }
                        Date d1 = new Date(date);
                        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                        DateFormat timeFormat = new SimpleDateFormat(" hh:mm a ");
                        String startDate = dateFormat.format(d1);
                        String startTime = timeFormat.format(d1);
                        assert messageType != null;
                        if (type.equals("2")) {
                            if (!helper.readSMSLogs(add, body, startDate, startTime)) {
                                if (helper.insertSMS(add, body, startTime, startDate, messageType)) {
                                    Log.e("Insereted", "is called");
                                    inserted = true;
                                }
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
        helper.close();
        return inserted;
    }
}
