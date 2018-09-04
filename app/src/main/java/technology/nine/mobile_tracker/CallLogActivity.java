package technology.nine.mobile_tracker;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import technology.nine.mobile_tracker.adapters.CallLogsRecyclerAdapter;
import technology.nine.mobile_tracker.data.CallLogsDBHelper;
import technology.nine.mobile_tracker.model.CallLogs;
import technology.nine.mobile_tracker.service.CallDetectService;
import technology.nine.mobile_tracker.service.SmsDetectingService;

public class CallLogActivity extends AppCompatActivity {
    CallLogsDBHelper helper;
    RecyclerView recyclerView;
    CallLogsRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<CallLogs> callLogs = new ArrayList<>();
    private static final int READ_CALL_LOG_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    Intent mServiceIntent;
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Log.e("OnReceive", "is called");
            fetch();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate", "is called");
        setContentView(R.layout.activity_call_log);
        recyclerView = findViewById(R.id.recycler_view);
        permissionStatus = this.getSharedPreferences("permissionStatus", MODE_PRIVATE);
        permissions();

        if (!isMyServiceRunning(SmsDetectingService.class)) {
            mServiceIntent = new Intent(CallLogActivity.this, SmsDetectingService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(mServiceIntent);
            }
            startService(mServiceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.e("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.e("isMyServiceRunning?", false + "");
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onStart", "is called");

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(receiver, new IntentFilter(CallDetectService.UPDATE_UI));

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("onResume", "is called");
        fetch();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("onRestart", "is called");

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("onPause", "is called");

    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.e("OnStop", "is called");
        try {
         // stopService(mServiceIntent);
        } catch (Exception e) {
            Log.e("Exception OnStop", String.valueOf(e));
            e.printStackTrace();

        }
    }


    @Override
    protected void onDestroy() {

        Log.e("onDestroy ", "is called");
        try {
            Log.e("onDestroyTry ", "is called");
            if (isMyServiceRunning(SmsDetectingService.class)){
                stopService(new Intent(CallLogActivity.this,SmsDetectingService.class
                ));
            }
        } catch (Exception e) {
            Log.e("Exception OnStop", String.valueOf(e));
            e.printStackTrace();

        }


        super.onDestroy();
    }

    private void fetch() {
        // Log.e("Fetch", "is called");
        helper = new CallLogsDBHelper(this);
        callLogs = helper.getAllCallLog();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new CallLogsRecyclerAdapter(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.addAll(callLogs);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void permissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CALL_LOG) +
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.READ_SMS) +
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(CallLogActivity.this,
                        Manifest.permission.READ_CALL_LOG)
                        || ActivityCompat.shouldShowRequestPermissionRationale(CallLogActivity.this,
                        Manifest.permission.READ_SMS)
                        || ActivityCompat.shouldShowRequestPermissionRationale(CallLogActivity.this,
                        Manifest.permission.RECEIVE_SMS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CallLogActivity.this);
                    builder.setTitle("Need Permissions");
                    builder.setTitle("This app  needs Read Call Log  and Read SMS Permission.");
                    builder.setPositiveButton("Grant ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(CallLogActivity.this, new String[]{Manifest.permission.READ_CALL_LOG,
                                            Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                                    READ_CALL_LOG_PERMISSION_CONSTANT);
                        }

                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(Manifest.permission.READ_CALL_LOG, false)
                        || permissionStatus.getBoolean(Manifest.permission.READ_SMS, false)
                        || permissionStatus.getBoolean(Manifest.permission.RECEIVE_SMS, false)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CallLogActivity.this);
                    builder.setTitle("Need Permissions");
                    builder.setMessage("This app  needs Read Call Log  and Read SMS Permission.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getBaseContext(), "Go to Permissions to Grant Read Call Logs and Read SMS", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(CallLogActivity.this, new String[]{Manifest.permission.READ_CALL_LOG,
                                    Manifest.permission.READ_SMS,
                                    Manifest.permission.RECEIVE_SMS},
                            READ_CALL_LOG_PERMISSION_CONSTANT);
                }
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(Manifest.permission.READ_CALL_LOG, true);
                editor.putBoolean(Manifest.permission.CALL_PHONE, true);
                editor.apply();
            } else {
                fetch();
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CALL_LOG_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                fetch();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(CallLogActivity.this,
                        Manifest.permission.READ_CALL_LOG)
                        || ActivityCompat.shouldShowRequestPermissionRationale(CallLogActivity.this,
                        Manifest.permission.READ_SMS)
                        || ActivityCompat.shouldShowRequestPermissionRationale(CallLogActivity.this,
                        Manifest.permission.RECEIVE_SMS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CallLogActivity.this);
                    builder.setTitle("Need  Permissions");
                    builder.setTitle("This app  needs Read Call Log and Read SMS Permission.");
                    builder.setPositiveButton("Grant ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(CallLogActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, READ_CALL_LOG_PERMISSION_CONSTANT);
                        }

                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                fetch();
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                fetch();
            }
        }
    }
}


