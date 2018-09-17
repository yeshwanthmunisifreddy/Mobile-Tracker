package technology.nine.mobile_tracker.service;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import java.util.Date;
import java.util.Objects;

import technology.nine.mobile_tracker.utils.DbBitmapUtility;
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

    private void insertData(StatusBarNotification sbn) {
        /*String packageName = statusBarNotification.getPackageName();
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
        int  style = statusBarNotification.describeContents();
        Date date = new Date();
        Log.e("Package", packageName +"");
        Log.e("Title", title +"");
        Log.e("Text", text +"");
        Log.e("key", key +"");
        Log.e("getId", String.valueOf(getId));
        Log.e("Ongoing", String.valueOf(ongoing));
        Log.e("Date", String.valueOf(date));
        helper.insertNotifications(packageName, title, text, String.valueOf(time), image);*/
        String pack = sbn.getPackageName();
        Bundle extras = sbn.getNotification().extras;
        String textlines = null;

        String ticker = null;
        if (sbn.getNotification().tickerText != null) {
            ticker = sbn.getNotification().tickerText.toString();
        }

        String title = null;
        if (extras.get(Notification.EXTRA_TITLE) != null) {
            title = extras.get(Notification.EXTRA_TITLE).toString();
        }

        String title_big = null;
        if (extras.get(Notification.EXTRA_TITLE_BIG) != null) {
            title_big = extras.get(Notification.EXTRA_TITLE_BIG).toString();
        }

        String text = null;
        if (extras.get(Notification.EXTRA_TEXT) != null) {
            text = extras.get(Notification.EXTRA_TEXT).toString();
        }

        String text_big = null;
        if (extras.get(Notification.EXTRA_BIG_TEXT) != null) {
            text_big = extras.get(Notification.EXTRA_BIG_TEXT).toString();
        }

        String summary = null;
        if(extras.get(Notification.EXTRA_SUMMARY_TEXT) != null) {
            summary = extras.get(Notification.EXTRA_SUMMARY_TEXT).toString();
        }

        String subtext = null;
        if (extras.get(Notification.EXTRA_SUB_TEXT) != null) {
            summary = extras.get(Notification.EXTRA_SUB_TEXT).toString();
        }

        String infotext = null;
        if (extras.get(Notification.EXTRA_INFO_TEXT) != null) {
            infotext = extras.get(Notification.EXTRA_INFO_TEXT).toString();
        }


        CharSequence[] textline = extras.getCharSequenceArray(Notification.EXTRA_TEXT_LINES);

        if (textline != null) {
            textlines = String.valueOf(textline.length)+"<br>- ";
            textlines += TextUtils.join("<br>- ", textline);
        }


        String tag = sbn.getTag();
        //String key = sbn.getKey();
        Boolean clear = sbn.isClearable();
        Boolean ongoing = sbn.isOngoing();
        Long time1 = sbn.getNotification().when;
        Boolean time2 = extras.getBoolean(Notification.EXTRA_SHOW_WHEN);
        Integer getid = sbn.getId();
        //String groupkey = sbn.getGroupKey();
        Long time3 = sbn.getPostTime();

        int id = extras.getInt(Notification.EXTRA_SMALL_ICON);
        Context remotePackageContext = null;
        Bitmap bmp = null;
        byte[] image = null;
        try {
            remotePackageContext = getApplicationContext().createPackageContext(pack, 0);
            Drawable icon = remotePackageContext.getResources().getDrawable(id);
            if (icon != null) {
                bmp = ((BitmapDrawable) icon).getBitmap();
                image = DbBitmapUtility.getBytes(bmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap picture = (Bitmap) extras.get(Notification.EXTRA_PICTURE);
        Bitmap largeIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON_BIG);
        Bitmap icon = (Bitmap) extras.get(Notification.EXTRA_BACKGROUND_IMAGE_URI);
        Bitmap bigIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
        String backgroundUrl = (String)extras.get(Notification.	EXTRA_BACKGROUND_IMAGE_URI);
        Log.e("pack",pack +"");
        Log.e("ticker",ticker+"");
        Log.e("title",title+"");
        Log.e("title_big",title_big+"");
        Log.e("text",text+"");
        Log.e("text_big",text_big+"");
        Log.e("summary",summary+"");

        Log.e("textlines",textlines+"");
        Log.e("tag",tag+"");
        Log.e("clear",clear+"");
        Log.e("ongoing",ongoing+"");
        Log.e("time1",time1+"");
        Log.e("getid",getid+"");
        Log.e("time3",time3+"");
        Log.e("Image",bmp+"");
        Log.e("BigIcon",bigIcon+"");
        Log.e("picture",picture+"");
    }
}

