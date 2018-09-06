package technology.nine.mobile_tracker;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

import technology.nine.mobile_tracker.service.SmsDetectingService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final int READ_CALL_LOG_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;
    Intent mServiceIntent;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionStatus = this.getSharedPreferences("permissionStatus", MODE_PRIVATE);
        permissions();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //loading default fragment
        Objects.requireNonNull(getSupportActionBar()).setTitle("Calls");
        CallLogsFragments callLogsFragments = new CallLogsFragments();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame,callLogsFragments)
                .addToBackStack(null)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //start background service
        if (!isMyServiceRunning(SmsDetectingService.class)) {
            mServiceIntent = new Intent(MainActivity.this, SmsDetectingService.class);
            if (Build.VERSION.SDK_INT >= 26) {
                startForegroundService(mServiceIntent);
            }
            startService(mServiceIntent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
            finish();
        }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
    }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.call_logs) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Calls");
            CallLogsFragments callLogsFragments = new CallLogsFragments();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame,callLogsFragments)
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.message_logs) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Messages");
            MessageLogFragment messageLogFragment= new MessageLogFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame,messageLogFragment)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.notification_logs) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Notifications");
            NotificationLogFragment notificationLogFragment= new NotificationLogFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame,notificationLogFragment)
                    .addToBackStack(null)
                    .commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onDestroy() {
        Log.e("onDestroy ", "is called");
        try {
            Log.e("onDestroyTry ", "is called");
            if (isMyServiceRunning(SmsDetectingService.class)){
                stopService(new Intent(MainActivity.this,SmsDetectingService.class
                ));
            }
        } catch (Exception e) {
            Log.e("Exception OnStop", String.valueOf(e));
            e.printStackTrace();
        }
        super.onDestroy();
    }
    //checking background service  running or not running
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
    //permissions for accessing the Call and SMS data
    public void permissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CALL_LOG) +
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.READ_SMS) +
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_CALL_LOG)
                        || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_SMS)
                        || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Need Permissions");
                    builder.setTitle("This app  needs Read Call Log  and Read SMS Permission.");
                    builder.setPositiveButton("Grant ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG,
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
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
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG,
                                    Manifest.permission.READ_SMS,
                                    Manifest.permission.RECEIVE_SMS},
                            READ_CALL_LOG_PERMISSION_CONSTANT);
                }
                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(Manifest.permission.READ_CALL_LOG, true);
                editor.putBoolean(Manifest.permission.CALL_PHONE, true);
                editor.apply();
            } else {

            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_CALL_LOG_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_CALL_LOG)
                        || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.READ_SMS)
                        || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Need  Permissions");
                    builder.setTitle("This app  needs Read Call Log and Read SMS Permission.");
                    builder.setPositiveButton("Grant ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_CALL_LOG}, READ_CALL_LOG_PERMISSION_CONSTANT);
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
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission

            }
        }
    }

}
