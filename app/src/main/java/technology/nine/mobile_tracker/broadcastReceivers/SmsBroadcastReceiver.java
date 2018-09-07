package technology.nine.mobile_tracker.broadcastReceivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import technology.nine.mobile_tracker.service.SmsDetectingService;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT < 24) {
            //after service stops ,start service again
            if (intent.getAction().equals("technology.nine.mobile_tracker.SMS_SERVICE")) {
                startIntentService(context);
            }
            //after boot complete start service
            if ((intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))) {
                startIntentService(context);
            }
        }
    }

    //start service
    public void startIntentService(Context context) {
        if (!myServiceRunning(context, SmsDetectingService.class)) {
            Intent serviceIntent = new Intent(context, SmsDetectingService.class);
            serviceIntent.setPackage("technology.nine.mobile_tracker");
            if (Build.VERSION.SDK_INT >= 26) {
                context.startForegroundService(serviceIntent);
            } else {
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
}
