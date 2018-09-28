package technology.nine.mobile_tracker.broadcastReceivers;

import android.app.ActivityManager;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Telephony;
import android.support.v4.app.JobIntentService;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import technology.nine.mobile_tracker.service.CallDetectService;
import technology.nine.mobile_tracker.service.IncomingSmsService;
import technology.nine.mobile_tracker.service.NotificationService;
import technology.nine.mobile_tracker.service.SmsDetectingService;

import static android.service.notification.NotificationListenerService.requestRebind;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //after service stops ,start service again
        if (intent.getAction().equals("technology.nine.mobile_tracker.SMS_SERVICE")) {
            startIntentService(context);
        }
        //after boot complete start service
        if ((intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))) {
            startIntentService(context);
            tryReconnectService(context);
        }
        //read incoming sms
        if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                String messageBody = smsMessage.getMessageBody();
                String number = smsMessage.getOriginatingAddress();
                long date = smsMessage.getTimestampMillis();
                String name = smsMessage.getDisplayMessageBody();
                //converting time in milliseconds to date and time format
                Date d1 = new Date(date);
                DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
                DateFormat timeFormat = new SimpleDateFormat(" hh:mm a ");
                String startDate = dateFormat.format(d1);
                String time = timeFormat.format(d1);
                Intent serviceIntent = new Intent(context, IncomingSmsService.class);
                serviceIntent.putExtra("MessageBody", messageBody);
                serviceIntent.putExtra("Number", number);
                serviceIntent.putExtra("Date", startDate);
                serviceIntent.putExtra("Time", time);
                if (Build.VERSION.SDK_INT >= 26) {
                 context.startForegroundService(serviceIntent);
                }
                else {
                    context.startService(serviceIntent);
                }
            }
        }

    }

    //start service
    public void startIntentService(Context context) {
        if (!myServiceRunning(context, SmsDetectingService.class)) {
            Intent serviceIntent = new Intent(context, SmsDetectingService.class);
            serviceIntent.setPackage("technology.nine.mobile_tracker");
            if (Build.VERSION.SDK_INT < 26) {
                context.startService(serviceIntent);
            }

        }

    }

    //check if service is already running below Api 24
    private boolean myServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void tryReconnectService(Context context) {
        toggleNotificationListenerService(context);
        Log.e("tryReconnectService", "is called");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            ComponentName componentName =
                    new ComponentName(context, NotificationService.class);

            //It say to Notification Manager RE-BIND your service to listen notifications again inmediatelly!
            requestRebind(componentName);
        }
    }

    private void toggleNotificationListenerService(Context context) {
        Log.e("toggleNotification", "is called");
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(context, NotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(context, NotificationService.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
}
