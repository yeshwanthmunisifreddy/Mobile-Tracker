package technology.nine.mobile_tracker;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import technology.nine.mobile_tracker.service.SmsDetectingService;

public class MessageContentObserver extends ContentObserver {
    private Context context;

    public MessageContentObserver(Context context) {
        super(new Handler());
        this.context = context;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        Log.e("OnChange", "is called");
        //Toast.makeText(context, "OnChange is called", Toast.LENGTH_SHORT).show();

        try {
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                insertSmsDatabase();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void  insertSmsDatabase(){
        try{
            Uri uri = Uri.parse("content://sms/sent");
            Cursor cursor =context.getContentResolver().query(uri,null,null,null,"date DESC limit 1");
            if (cursor != null){
                try {
                    while (cursor.moveToNext()){
                        String body = cursor.getString(cursor.getColumnIndex("body")); //content of sms
                        String add = cursor.getString(cursor.getColumnIndex("address")); //phone num
                        String date = cursor.getString(cursor.getColumnIndex("date")); //date
                        String time = cursor.getString(cursor.getColumnIndex(""));
                        String protocol = cursor.getString(cursor.getColumnIndex("protocol"));
                        Log.e("SMS Sent",body +" "+ add + " "+ time + " "+ protocol );
                    }
                }finally {
                    cursor.close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

