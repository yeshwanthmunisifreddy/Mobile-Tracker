package technology.nine.mobile_tracker.utils;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import technology.nine.mobile_tracker.service.SmsJobServices;

import static android.content.Context.JOB_SCHEDULER_SERVICE;

public class Util {
    public static void scheduleJob(Context context) {
        ComponentName componentName = new ComponentName(context, SmsJobServices.class);
        JobInfo info = null;
        Uri SMS_URI = Uri.parse("content://sms");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            info = new JobInfo.Builder(123, componentName)
                    .setMinimumLatency(0)
                    .addTriggerContentUri(new JobInfo.TriggerContentUri(
                            Telephony.Sms.CONTENT_URI,
                            JobInfo.TriggerContentUri.FLAG_NOTIFY_FOR_DESCENDANTS))
                    .addTriggerContentUri(new JobInfo.TriggerContentUri(SMS_URI, 0))
                    .build();
            JobScheduler jobScheduler = (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
            int res = jobScheduler.schedule(info);
            if (res == JobScheduler.RESULT_SUCCESS) {
                Log.e("Jobschedule", "is success");
            } else {
                Log.e("Jobschedule", "is  failed");
            }
        }


    }
}
