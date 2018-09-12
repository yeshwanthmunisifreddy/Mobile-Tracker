package technology.nine.mobile_tracker.service;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import java.util.Date;
import java.util.Objects;

import technology.nine.mobile_tracker.DbBitmapUtility;
import technology.nine.mobile_tracker.data.LogsDBHelper;

public class NotificationService extends NotificationListenerService {
    LogsDBHelper helper = new LogsDBHelper(NotificationService.this);

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        insertData(sbn);


    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }

    @Override
    public StatusBarNotification[] getActiveNotifications() {
        return super.getActiveNotifications();
    }

    @Override
    public StatusBarNotification[] getActiveNotifications(String[] keys) {
        return super.getActiveNotifications(keys);
    }

    private void insertData(StatusBarNotification statusBarNotification) {
        if (!statusBarNotification.isOngoing()) {
            String packageName = statusBarNotification.getPackageName();
            Bundle extras = statusBarNotification.getNotification().extras;
            String title = null;
            if (extras.get(Notification.EXTRA_TITLE) != null) {
                title = Objects.requireNonNull(extras.get(Notification.EXTRA_TITLE)).toString();
            }
            String text = null;
            if (extras.get(Notification.EXTRA_TEXT) != null) {
                text = Objects.requireNonNull(extras.get(Notification.EXTRA_TEXT)).toString();
            }
            int id = extras.getInt(Notification.EXTRA_SMALL_ICON);
            Context remotePackageContext = null;
            Bitmap bmp = null;
            byte[] image = null;
            try {
                remotePackageContext = getApplicationContext().createPackageContext(packageName, 0);
                Drawable icon = remotePackageContext.getResources().getDrawable(id);
                if (icon != null) {
                    bmp = ((BitmapDrawable) icon).getBitmap();
                    image = DbBitmapUtility.getBytes(bmp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            String key = statusBarNotification.getKey();
            Long time = statusBarNotification.getNotification().when;
            Integer getId = statusBarNotification.getId();
            Boolean ongoing = statusBarNotification.isOngoing();
            Date date = new Date();
            Log.e("Package", packageName);
            Log.e("Title", title);
            Log.e("Text", text);
            Log.e("key", key);
            Log.e("getId", String.valueOf(getId));
            Log.e("Ongoing", String.valueOf(ongoing));
            Log.e("Date", String.valueOf(date));
            helper.insertNotifications(packageName, title, text, String.valueOf(time), image);
        }
    }
}
