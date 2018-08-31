package technology.nine.mobile_tracker.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import technology.nine.mobile_tracker.data.CallLogsDBHelper;

public class CallDetectService extends Service {


    public CallDetectService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int res = super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            CallLogsDBHelper helper = new CallLogsDBHelper(CallDetectService.this);
            String phoneNumber = intent.getStringExtra("PhoneNumber");
            String startDate = intent.getStringExtra("StartDate");
            String startTime = intent.getStringExtra("StartTime");
            String duration = intent.getStringExtra("Duration");
            String callType = intent.getStringExtra("CallType");
            helper.insertCalllogs(phoneNumber,callType,startDate,startTime,duration);
        }
        return res;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
