package technology.nine.mobile_tracker;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class MessageContentObserver extends ContentObserver {
    private  Context context;
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
        Log.e("OnChange","is called");
        //Toast.makeText(context, "OnChange is called", Toast.LENGTH_SHORT).show();
    }
}

