package technology.nine.mobile_tracker.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import technology.nine.mobile_tracker.service.CallDetectService;

public class CallLogBroadcastReceiver extends BroadcastReceiver {
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    private static boolean isIncoming;
    private static String savedNumber;


    @Override
    public void onReceive(Context context, Intent intent) {
        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (Objects.equals(intent.getAction(), "android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = Objects.requireNonNull(intent.getExtras()).getString("android.intent.extra.PHONE_NUMBER");
        } else {
            String stateStr = Objects.requireNonNull(intent.getExtras()).getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            assert stateStr != null;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallStateChanged(context, state, number);
        }
    }

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
            //No change, deBounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    //  Log.e("hook", "is called");

                }
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = true;
                    callStartTime = new Date();
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    intentService(context, savedNumber, callStartTime, null, "Missed call");
                } else if (isIncoming) {
                    intentService(context, savedNumber, callStartTime, new Date(), "Incoming call");
                } else {
                    intentService(context, savedNumber, callStartTime, new Date(), "Outgoing call");
                }
                break;
        }
        lastState = state;
    }

    private void intentService(Context ctx, String number, Date start, Date end, String callType) {
        //get call information PhoneStateListener
      /*  DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        String t1 = " ";
        String t2 = " ";
        Date d1 = null;
        Date d2 = null;
        String duration = "";
        long diff;

        if (start != null && end != null) {
            try {
                d1 = format.parse(dateFormat1.format(start));
                d2 = format.parse(dateFormat1.format(end));
                diff = d2.getTime() - d1.getTime();
                int minutes = (int) (diff / (60 * 1000) % 60);
                if (minutes != 0) {
                    duration = (diff / (60 * 1000) % 60) + "m" + " " + (diff / 1000 % 60) + "sec";
                } else {
                    duration = (diff / 1000 % 60) + "sec";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }*/
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        DateFormat timeFormat = new SimpleDateFormat(" hh:mm a ");
        String startDate = "";
        String startTime = " ";
        if (start != null) {
            startDate = dateFormat.format(start);
            startTime = timeFormat.format(start);
        }
        Intent serviceIntent = new Intent(ctx, CallDetectService.class);
        serviceIntent.putExtra("StartDate", startDate);
        serviceIntent.putExtra("StartTime", startTime);
        if (Build.VERSION.SDK_INT >= 26) {
            ctx.startForegroundService(serviceIntent);
        } else {
            ctx.startService(serviceIntent);
        }
    }
}

