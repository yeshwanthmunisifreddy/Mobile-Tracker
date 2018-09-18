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
import technology.nine.mobile_tracker.model.NotificationLogs;
import technology.nine.mobile_tracker.model.Notifications;
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
            APP_NAME + " TEXT, " +
            TITLE + " TEXT, " +
            BIG_TITLE + " TEXT, " +
            TEXT + " TEXT, " +
            BIG_TEXT + " TEXT, " +
            SUMMARY + " TEXT, " +
            DATE + " TEXT, " +
            SMALL_ICON + " BLOB, " +
            BIG_ICON + " BLOB, " +
            PICTURE + " BLOB);";

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
        List<SmsLogs> smsLogs = new ArrayList<>();
        LinkedHashMap<String, ArrayList<SmsLogs>> linkedHashMap = new LinkedHashMap<>();
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

                if (!linkedHashMap.containsKey(number)) {
                    ArrayList<SmsLogs> messages = new ArrayList<>();
                    messages.add(new SmsLogs(number, body, time, date, type));
                    linkedHashMap.put(number, messages);
                }

            }
        } finally {
            Set s = linkedHashMap.entrySet();
            Iterator iterator = s.iterator();
            while (iterator.hasNext()) {
                Map.Entry m1 = (Map.Entry) iterator.next();
                ArrayList<SmsLogs> smsLogs1 = (ArrayList<SmsLogs>) m1.getValue();
                smsLogs.addAll(smsLogs1);
            }
            cursor.close();

        }
        return smsLogs;
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
        Log.e("ReadSmsLogs", cursor.getCount() + "");
        if (cursor.getCount() != 0) {
            returnValue = true;
            cursor.close();
        }
        return returnValue;


    }

    //Inserting Notifications into Database
    public boolean insertNotifications(String packageName, String appName, String title, String bigTitle, String text,
                                       String bigText, String summary, String date, byte[] smallIcon, byte[] bigIcon, byte[] extraPicture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PACKAGE_NAME, packageName);
        values.put(APP_NAME, appName);
        values.put(TITLE, title);
        values.put(BIG_TITLE, bigTitle);
        values.put(TEXT, text);
        values.put(BIG_TEXT, bigText);
        values.put(SUMMARY, summary);
        values.put(DATE, date);
        values.put(SMALL_ICON, smallIcon);
        values.put(BIG_ICON, bigIcon);
        values.put(PICTURE, extraPicture);
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

    public List<NotificationLogs> getAllNotifications() {
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        List<NotificationLogs> notificationLogs = new ArrayList<>();
        LinkedHashMap<String, ArrayList<NotificationLogs>> linkedHashMap = new LinkedHashMap<>();
        Cursor cursor = readableDatabase.query(NOTIFICATION_TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                _ID + " DESC "
        );
        try {
            while (cursor.moveToNext()) {
                String packageName = cursor.getString(cursor.getColumnIndexOrThrow(PACKAGE_NAME));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(TEXT));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
                String appName = cursor.getString(cursor.getColumnIndexOrThrow(APP_NAME));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow(SMALL_ICON));
                if (!linkedHashMap.containsKey(packageName)) {
                    ArrayList<NotificationLogs> notifications = new ArrayList<>();
                    notifications.add(new NotificationLogs(packageName, title, text, date, appName,image));
                    linkedHashMap.put(packageName, notifications);
                }

            }
        } finally {
            Set s = linkedHashMap.entrySet();
            Iterator iterator = s.iterator();
            while (iterator.hasNext()) {
                Map.Entry m1 = (Map.Entry) iterator.next();
                ArrayList<NotificationLogs> notificationLogsArrayList = (ArrayList<NotificationLogs>) m1.getValue();
                notificationLogs.addAll(notificationLogsArrayList);
            }
            cursor.close();
        }
        return notificationLogs;
    }

    public List<Notifications> getEachPackageNotifications(String packageName) {
        Log.e("PackageNotifications",packageName +"");
        SQLiteDatabase db = this.getReadableDatabase();
        List<Notifications> notifications = new ArrayList<>();
        String selection = APP_NAME + " = ?";
        String[] selectionArgs = new String[]{packageName};
        Cursor cursor = db.query(NOTIFICATION_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        try {
            while (cursor.moveToNext()) {
                String pack = cursor.getString(cursor.getColumnIndexOrThrow(PACKAGE_NAME));
                String appName = cursor.getString(cursor.getColumnIndexOrThrow(APP_NAME));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE));
                String bigTitle = cursor.getString(cursor.getColumnIndexOrThrow(BIG_TITLE));
                String text = cursor.getString(cursor.getColumnIndexOrThrow(TEXT));
                String bigText = cursor.getString(cursor.getColumnIndexOrThrow(BIG_TEXT));
                String summary = cursor.getString(cursor.getColumnIndexOrThrow(SUMMARY));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
                byte[] smallIcon = cursor.getBlob(cursor.getColumnIndexOrThrow(SMALL_ICON));
                byte[] bigIcon = cursor.getBlob(cursor.getColumnIndexOrThrow(BIG_ICON));
                byte[] extraPicture = cursor.getBlob(cursor.getColumnIndexOrThrow(PICTURE));
                notifications.add(new Notifications(pack, appName, title, bigTitle, text, bigText, summary, date, smallIcon, bigIcon, extraPicture));
            }
        }finally {
            cursor.close();
        }
        return notifications;
    }

    public List<SmsLogs> getAllEachNumber(String number) {
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        List<SmsLogs> smsLogs = new ArrayList<>();
        String selection = NUMBER + " = ?";
        String[] selectionArgs = new String[]{number};
        Cursor cursor = readableDatabase.query(SMS_TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        try {
            while (cursor.moveToNext()) {
                String phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(NUMBER));
                String body = cursor.getString(cursor.getColumnIndexOrThrow(BODY));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(TIME));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(SMS_TYPE));
                smsLogs.add(new SmsLogs(phoneNumber, body, time, date, type));
            }
        } finally {
            cursor.close();
        }
        return smsLogs;
    }

}
