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
import technology.nine.mobile_tracker.model.UserData;

import static technology.nine.mobile_tracker.data.CallLogsContract.*;
import static technology.nine.mobile_tracker.data.CallLogsContract.CallLogsEntry.*;
import static technology.nine.mobile_tracker.data.CallLogsContract.CallLogsEntry.TIME;

public class CallLogsDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "callLogs.db";
    private static final int DATABASE_VERSION = 1;

    public CallLogsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      /*  String SQL_CREATE_CALLLOGS_TABLE = " CREATE TABLE " + TABLE_NAME + "(" +
                _ID + " INTERGER PRIMARY KEY, " +
                NUMBER + " TEXT, " +
                CALL_TYPE + " TEXT, " +
                DATE + " Text, " +
                TIME + " TEXT, " +
                DURATION + " Text);";
        db.execSQL(SQL_CREATE_CALLLOGS_TABLE);*/

        String SQL_CREATE_CALL_LOG_TABLE = " CREATE TABLE " + CALL_LOG_TABLE_NAME + "(" +
                _ID + " INTERGER PRIMARY KEY, " +
                NAME + " TEXT, " +
                NUMBER + " TEXT, " +
                CALL_TYPE + " TEXT, " +
                DATE + " Text, " +
                TIME + " TEXT, " +
                DURATION + " Text);";
        db.execSQL(SQL_CREATE_CALL_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

  /*  //getting data from broadcast receiver and inserting call logs into callLogs Database table
    public boolean insertCalllogs(String number, String callType, String date, String time, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NUMBER, number);
        values.put(CALL_TYPE, callType);
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(DURATION, duration);
        return (db.insert(TABLE_NAME, null, values) != -1);
    }*/

    //getting data from Calls.Content_URI and inserting call logs into Call Log Database table
    public boolean insertCallLog(String name, String phoneNumber, String callType, String date, String time, String duration) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME, name);
        values.put(NUMBER, phoneNumber);
        values.put(CALL_TYPE, callType);
        values.put(DATE, date);
        values.put(TIME, time);
        values.put(DURATION, duration);
        return (db.insert(CALL_LOG_TABLE_NAME, null, values) != -1);

    }

   /* //reading data from the table calllogs from database
    public List<UserData> getAllCallLogs() {
        List<UserData> userData = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
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
                String callType = cursor.getString(cursor.getColumnIndexOrThrow(CALL_TYPE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(DATE));
                String time = cursor.getString(cursor.getColumnIndexOrThrow(TIME));
                String duration = cursor.getString(cursor.getColumnIndexOrThrow(DURATION));
                userData.add(new UserData(number, callType, date, time, duration));
            }
        } finally {
            cursor.close();
        }
        return userData;
    }
*/
    //reading data from the table CallLog from database
    public List<CallLogs> getAllCallLog() {
        List<CallLogs> callLogs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(CALL_LOG_TABLE_NAME,
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
                Log.e("CallLog", name + " " + number + " " + callType + " " + date + " " +time + " "+ duration);

                callLogs.add(new CallLogs(name, number, callType, date, time, duration));
            }
        }finally {
            cursor.close();
        }
       return callLogs;
    }


}
