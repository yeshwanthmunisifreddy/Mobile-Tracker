package technology.nine.mobile_tracker.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

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
    private static Date answeringStartTime;


    @Override
    public void onReceive(Context context, Intent intent) {
        //  Log.e("OnReceive", "is called");
        //We listen to two intents.  The new outgoing call only tells us of an outgoing call.  We use it to get the number.
        if (Objects.equals(intent.getAction(), "android.intent.action.NEW_OUTGOING_CALL")) {
            //  Log.e("if", "is called");
            savedNumber = Objects.requireNonNull(intent.getExtras()).getString("android.intent.extra.PHONE_NUMBER");
        } else {
            // Log.e("else", "is called");
            String stateStr = Objects.requireNonNull(intent.getExtras()).getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            int state = 0;
            //  Log.e("State", stateStr);
            assert stateStr != null;
            if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                //Log.e("IDLE", "is called");
                state = TelephonyManager.CALL_STATE_IDLE;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                //   Log.e("HOOk", "is called");
                state = TelephonyManager.CALL_STATE_OFFHOOK;
            } else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                //  Log.e("RINGING", "is called");
                state = TelephonyManager.CALL_STATE_RINGING;
            }

            onCallStateChanged(context, state, number);
        }
    }

    //Derived classes should override these to respond to specific events of interest
    protected void onIncomingCallStarted(Context ctx, String number, Date start) {
        // Log.e("onIncomingCallStarted", "is called");
    }

    protected void onOutgoingCallStarted(Context ctx, String number, Date start) {
        // Log.e("onOutgoingCallStarted", "is called");
    }

    protected void onIncomingCallEnded(Context ctx, String number, Date start, Date end) {
        //  Log.e("onIncomingCallEnded", "is called");
        intentService(ctx, number, start, end, "Incoming call");

    }

    protected void onOutgoingCallEnded(Context ctx, String number, Date start, Date end) {
        // Log.e("onOutgoingCallEnded", "is called");
        intentService(ctx, number, start, end, "Outgoing call");
    }

    protected void onMissedCall(Context ctx, String number, Date start) {
        // Log.e("onMissedCall", "is called");
        intentService(ctx, number, start, null, "Missed call");
    }

    //Deals with actual events

    //Incoming call-  goes from IDLE to RINGING when it rings, to OFFHOOK when it's answered, to IDLE when its hung up
    //Outgoing call-  goes from IDLE to OFFHOOK when it dials out, to IDLE when hung up
    public void onCallStateChanged(Context context, int state, String number) {
        if (lastState == state) {
            //No change, debounce extras
            return;
        }
        switch (state) {
            case TelephonyManager.CALL_STATE_RINGING:
                isIncoming = true;
                callStartTime = new Date();
                savedNumber = number;
                onIncomingCallStarted(context, number, callStartTime);
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                //Transition of ringing->offhook are pickups of incoming calls.  Nothing done on them
                if (lastState != TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = false;
                    callStartTime = new Date();
                    //  Log.e("hook", "is called");
                    onOutgoingCallStarted(context, savedNumber, callStartTime);
                }
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    isIncoming = true;
                    callStartTime = new Date();
                }
                break;
            case TelephonyManager.CALL_STATE_IDLE:
                //Went to idle-  this is the end of a call.  What type depends on previous state(s)
                if (lastState == TelephonyManager.CALL_STATE_RINGING) {
                    onMissedCall(context, savedNumber, callStartTime);
                    //Ring but no pickup-  a miss
                    //  Log.e("missed", "is called");
                } else if (isIncoming) {

                    onIncomingCallEnded(context, savedNumber, callStartTime, new Date());
                    //  Log.e("incomingRinging", "is called");
                } else {
                    onOutgoingCallEnded(context, savedNumber, callStartTime, new Date());
                    //    Log.e("outgoingRinging", "is called");
                }
                break;
        }
        lastState = state;
    }

    private void intentService(Context ctx, String number, Date start, Date end, String callType) {
        // Log.e("IntentService","is called");
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        DateFormat timeFormat = new SimpleDateFormat(" hh:mm a ");
        DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        String startDate = "";
        String startTime = " ";
        String endDate = " ";
        String endTime = " ";
        String t1 = " ";
        String t2 = " ";
        Date d1 = null;
        Date d2 = null;
        String duration = "";
        long diff;
        if (start != null) {
            startDate = dateFormat.format(start);
            startTime = timeFormat.format(start);
            t1 = dateFormat1.format(start);
            // Log.e("T1", t1);

        }
        if (end != null) {
            endDate = dateFormat.format(end);
            endTime = timeFormat.format(end);
            t2 = dateFormat1.format(end);
            // Log.e("T2", t1);
        }
        if (start != null && end != null) {
            try {
                d1 = format.parse(dateFormat1.format(start));
                d2 = format.parse(dateFormat1.format(end));
                diff = d2.getTime() - d1.getTime();
                // Log.e("Diff ", String.valueOf(diff));
                int minutes = (int) (diff / (60 * 1000) % 60);
                if (minutes != 0) {
                    duration = (diff / (60 * 1000) % 60) + "m" + " " + (diff / 1000 % 60) + "sec";
                } else {
                    duration = (diff / 1000 % 60) + "sec";
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Intent serviceIntent = new Intent(ctx, CallDetectService.class);
        serviceIntent.putExtra("PhoneNumber", number);
        serviceIntent.putExtra("CallType", callType);
        serviceIntent.putExtra("StartDate", startDate);
        serviceIntent.putExtra("StartTime", startTime);
        serviceIntent.putExtra("Duration", duration);
        if (Build.VERSION.SDK_INT >= 26) {
            ctx.startForegroundService(serviceIntent);
        } else {
            ctx.startService(serviceIntent);
        }
    }
}

