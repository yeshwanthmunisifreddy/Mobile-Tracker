package technology.nine.mobile_tracker.broadcastReceivers;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import java.util.List;

import technology.nine.mobile_tracker.service.SmsDetectingService;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String SMS_SENT = "android.provider.Telephony.SMS_SENT";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Action",intent.getAction());
        if (intent.getAction().equals("technology.nine.mobile_tracker.SMS_SERVICE")){
            startIntentService(context);
        }
        if ((intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))){
            startIntentService(context);
        }

       /* Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        if (bundle != null)
        {
            //---retrieve the SMS message received---
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i=0; i<msgs.length; i++){
                msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                str += "SMS from " + msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody().toString();
                str += "n";
            }
            //---display the new SMS message---
            Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
            Log.e("sms",str);
        }*/
    }public  void startIntentService(Context context){
        if (!myServiceRunning(context,SmsDetectingService.class)){
            Intent  serviceIntent =  new Intent(context, SmsDetectingService.class);
            serviceIntent.setPackage("technology.nine.mobile_tracker");
            if (Build.VERSION.SDK_INT >= 26){
                context.startForegroundService(serviceIntent);
            }
            else {
                context.startService(serviceIntent);
            }
        }

    }
    private boolean myServiceRunning(Context context ,Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.e("MyServiceRunning?", true + "");
                return true;
            }
        }
        Log.e("MyServiceRunning?", false + "");
        return false;
    }
}
