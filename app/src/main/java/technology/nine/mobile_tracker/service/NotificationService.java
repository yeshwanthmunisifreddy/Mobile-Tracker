package technology.nine.mobile_tracker.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import technology.nine.mobile_tracker.utils.DbBitmapUtility;
import technology.nine.mobile_tracker.data.LogsDBHelper;


public class NotificationService extends NotificationListenerService {
    public static final String UPDATE_NOTIFICATIONS_LOGS_UI = "updateNotificationUi";
    LogsDBHelper helper = new LogsDBHelper(NotificationService.this);

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Log.e("onNotificationPosted","is called");

        try {
            insertData(sbn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            helper.close();
        }


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
        String pack = sbn.getPackageName();
        Notification notification = sbn.getNotification();
        int color = notification.color;

        Bundle extras = sbn.getNotification().extras;
        String textlines = null;
        int id = extras.getInt(Notification.EXTRA_SMALL_ICON);
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
        if (extras.get(Notification.EXTRA_SUMMARY_TEXT) != null) {
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
            textlines = String.valueOf(textline.length) + "<br>- ";
            textlines += TextUtils.join("<br>- ", textline);
        }
        byte[] small_icon = new byte[0];
        Long time1 = sbn.getNotification().when;
        Long time3 = sbn.getPostTime();
        if (time1 == null) {
            time1 = time3;
        }
        if (text == null) {
            text = textlines;
        }
        Bitmap picture = (Bitmap) extras.get(Notification.EXTRA_PICTURE);
        Bitmap bigIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
        String backgroundUrl = (String) extras.get(Notification.EXTRA_BACKGROUND_IMAGE_URI);
        String appName = null;
        PackageManager packageManager = getApplicationContext().getPackageManager();
        try {
            appName = (String) packageManager.getApplicationLabel(packageManager.getApplicationInfo(pack, PackageManager.GET_META_DATA));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //converting bitmap to byte array
        byte[] large_icon = null;
        if (bigIcon != null) {
            large_icon = DbBitmapUtility.getBytes(bigIcon);
        }
        byte[] extra_picture = null;
        if (picture != null) {
            extra_picture = DbBitmapUtility.getBytes(picture);
        }
        if (title == null) {
            title = "unknown";
        }
        if (text == null) {
            text = "unknown";
        }
        if (!helper.readNotifications(pack, title, text, String.valueOf(time1))) {
            if (helper.insertNotifications(pack, appName, title, title_big, text, text_big, summary, String.valueOf(time1), small_icon, large_icon, extra_picture))
                updateUi();


        }
    }

    public static Bitmap getBitmap(Drawable drawable) {
        Bitmap createBitmap;
        Canvas canvas;
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if (drawable.getIntrinsicWidth() > 0) {
            if (drawable.getIntrinsicHeight() > 0) {
                createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                canvas = new Canvas(createBitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return createBitmap;
            }
        }
        createBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }

    public void updateUi() {
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(new Intent(UPDATE_NOTIFICATIONS_LOGS_UI));

    }
}

