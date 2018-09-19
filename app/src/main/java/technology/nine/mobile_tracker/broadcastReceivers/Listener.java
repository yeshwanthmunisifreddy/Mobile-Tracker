package technology.nine.mobile_tracker.broadcastReceivers;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Objects;

import technology.nine.mobile_tracker.MainActivity;

public class Listener extends BroadcastReceiver {
    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
        String pwd = Objects.requireNonNull(intent.getData()).getHost();
        if (pwd.equals("7777")) {
            Intent i = new Intent(context, MainActivity.class);
            i.putExtra("data", pwd);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

        }
    }
}
