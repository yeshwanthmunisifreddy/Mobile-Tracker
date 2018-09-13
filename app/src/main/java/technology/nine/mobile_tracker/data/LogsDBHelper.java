package technology.nine.mobile_tracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import technology.nine.mobile_tracker.model.CallLogs;
import technology.nine.mobile_tracker.model.SmsLog;
import technology.nine.mobile_tracker.model.SmsLogs;

import static technology.nine.mobile_tracker.data.LogsContract.LogsEntry.*;
import static technology.nine.mobile_tracker.data.LogsContract.LogsEntry.TIME;

public class LogsDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Logs.db";
    private static final int DATABASE_VERSION = 1;

    public LogsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private String SQL_CREATE_CALL_LOG_TABLE = " CREATE TABLE " + CALL_LOG_TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY, " +
            NAME + " TEXT, " +
            NUMBER + " TEXT, " +
            CALL_TYPE + " TEXT, " +
            DATE + " Text, " +
            TIME + " TEXT, " +
            DURATION + " Text);";

    private String SQL_CREATE_SMS_TABLE = " CREATE TABLE " + SMS_TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY, " +
            NUMBER + " TEXT, " +
            BODY + " TEXT, " +
            SMS_TYPE + " TEXT, " +
            DATE + " Text, " +
            TIME + " TEXT);";

    private String SQL_CREATE_NOTIFICATION_TABLE = " CREATE TABLE " + NOTIFICATION_TABLE_NAME + "(" +
            _ID + " INTEGER PRIMARY KEY, " +
            PACKAGE_NAME + " TEXT, " +
            TITLE + " TEXT, " +
            TEXT + " TEXT, " +
            DATE + " TEXT, " +
            IMAGE + " BLOB);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CALL_LOG_TABLE);
        db.execSQL(SQL_CREATE_SMS_TABLE);
        db.execSQL(SQL_CREATE_NOTIFICATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Inserting Call logs into Database
    public boolean insertCallLog(String name, String phoneNumber, String callType, String date, String time, String duration) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(NUMBER, phoneNumber);
        values.put(CALL_TYPE, callType);
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(DURATION, duration);
        return (writableDatabase.insert(CALL_LOG_TABLE_NAME, null, values) != -1);
    }

    // Insert SMS data into Database
    public boolean insertSMS(String phoneNumber, String body, String time, String date, String type) {
        SQLiteDatabase writableDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NUMBER, phoneNumber);
        values.put(BODY, body);
        values.put(TIME, time);
        values.put(DATE, date);
        values.put(SMS_TYPE, type);
        return (writableDatabase.insert(SMS_TABLE_NAME, null, values) != -1);

    }

    //Reading data from Database
    public List<CallLogs> getAllCallLog() {
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        List<CallLogs> callLogs = new ArrayList<>();
        Cursor cursor = readableDatabase.query(CALL_LOG_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC "
        );
        try {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(NAME));
                String number = cursor.getString(cursor.getColumnIndexOrThrow(NUMBER));
                String callType = cursor.getString(cursor.getColumnIndexOrThrow(CALL_TYPE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(TIME));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(DURATION));
                //  Log.e("CallLog", name + " " + number + " " + callType + " " + date + " " +time + " "+ duration);
                callLogs.add(new CallLogs(name, number, callType, date, time, duration));
            }
        } finally {
            cursor.close();
        }
        return callLogs;
    }

    //Reading SMS from Database
    public List<SmsLogs> getAllSMS() {
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        ArrayList<SmsLog> arrayLists = new ArrayList<>();
        List<SmsLogs> smsLogss = new ArrayList<>();
        LinkedHashMap<String, ArrayList<SmsLogs>> smsLogs = new LinkedHashMap<>();
        Cursor cursor = readableDatabase.query(SMS_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC "
        );
        try {
            while (cursor.moveToNext()) {
                String number = cursor.getString(cursor.getColumnIndexOrThrow(NUMBER));
                String body = cursor.getString(cursor.getColumnIndexOrThrow(BODY));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(TIME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(SMS_TYPE));

                if (smsLogs.containsKey(number)) {

                } else {
                    ArrayList<SmsLogs> messages = new ArrayList<>();
                    messages.add(new SmsLogs(number, body, time, date, type));
                    smsLogs.put(number, messages);
                }

            }
        } finally {
            Set s = smsLogs.entrySet();
            Iterator iterator = s.iterator();
            while (iterator.hasNext()) {
                Map.Entry m1 = (Map.Entry) iterator.next();
                ArrayList<SmsLogs> smsLogs1 = (ArrayList<SmsLogs>) m1.getValue();
                smsLogss.addAll(smsLogs1);
            }
            cursor.close();

        }
        return smsLogss;
    }


    //Check if sms is already added to Database
    public boolean readSMSLogs(String number, String body, String date, String time) {
        boolean returnValue = false;
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        String selection = NUMBER + " = ?" + " AND " + BODY + " = ?" + " AND " + DATE + " = ?" + " AND " + TIME + " = ?";
        String[] selectionArgs = new String[]{number, body, date, time};
        Cursor cursor = readableDatabase.query(SMS_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.getCount() != 0) {
            returnValue = true;
            cursor.close();
        }
        return returnValue;


    }

    //Inserting Notifications into Database
    public boolean insertNotifications(String packageName, String title, String text, String date, byte[] image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PACKAGE_NAME, packageName);
        values.put(TITLE, title);
        values.put(TEXT, text);
        values.put(DATE, date);
        values.put(IMAGE, image);
        return (db.insert(NOTIFICATION_TABLE_NAME, null, values) != -1);
    }

    //Check if Notification is already added to Database
    public boolean readNotifications(String packageName, String title, String text, String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean retrunValue = false;
        String selection = PACKAGE_NAME + " = ?" + " AND " + TITLE + " = ?" + " AND " + TEXT + " = ?" + " AND " + DATE + " = ?";
        String[] selectionArgs = new String[]{packageName, title, text, date};
        Cursor cursor = db.query(NOTIFICATION_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null);
        if (cursor.getCount() != 0) {
            retrunValue = true;
            cursor.close();
        }
        return retrunValue;
    }

}
