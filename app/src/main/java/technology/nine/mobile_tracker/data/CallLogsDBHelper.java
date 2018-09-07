package technology.nine.mobile_tracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import technology.nine.mobile_tracker.model.CallLogs;
import technology.nine.mobile_tracker.model.SmsLogs;
import technology.nine.mobile_tracker.model.UserData;

import static technology.nine.mobile_tracker.MessageContentObserver.COLUMN_ADDRESS;
import static technology.nine.mobile_tracker.MessageContentObserver.COLUMN_BODY;
import static technology.nine.mobile_tracker.MessageContentObserver.COLUMN_DATE;
import static technology.nine.mobile_tracker.MessageContentObserver.COLUMN_DATE_SENT;
import static technology.nine.mobile_tracker.MessageContentObserver.COLUMN_PROTOCOL;
import static technology.nine.mobile_tracker.data.CallLogsContract.*;
import static technology.nine.mobile_tracker.data.CallLogsContract.CallLogsEntry.*;
import static technology.nine.mobile_tracker.data.CallLogsContract.CallLogsEntry.TIME;

public class CallLogsDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "callLogs.db";
    private static final int DATABASE_VERSION = 1;

    public CallLogsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    String SQL_CREATE_CALL_LOG_TABLE = " CREATE TABLE " + CALL_LOG_TABLE_NAME + "(" +
            _ID + " INTERGER PRIMARY KEY, " +
            NAME + " TEXT, " +
            NUMBER + " TEXT, " +
            CALL_TYPE + " TEXT, " +
            DATE + " Text, " +
            TIME + " TEXT, " +
            DURATION + " Text);";
    String SQL_CREATE_SMS_TABLE = " CREATE TABLE " + SMS_TABLE_NAME + "(" +
            _ID + " INTERGER PRIMARY KEY, " +
            NUMBER + " TEXT, " +
            BODY + " TEXT, " +
            SMS_TYPE + " TEXT, " +
            DATE + " Text, " +
            TIME + " TEXT);";

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_CALL_LOG_TABLE);
        db.execSQL(SQL_CREATE_SMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    //getting data from Calls.Content_URI and inserting call logs into Call Log Database table
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

    //reading data from the table CallLog from database
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

    public List<SmsLogs> getAllSMS() {
        SQLiteDatabase readableDatabase = this.getReadableDatabase();
        List<SmsLogs> smsLogs = new ArrayList<>();
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
                smsLogs.add(new SmsLogs(number, body, time, date, type));

            }
        } finally {
            cursor.close();
        }
        return smsLogs;
    }

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
        Log.e("CursorValue", String.valueOf(cursor.getCount()));
        if (cursor.getCount() != 0) {
            returnValue = true;
            cursor.close();
        }
        return returnValue;


    }
}
