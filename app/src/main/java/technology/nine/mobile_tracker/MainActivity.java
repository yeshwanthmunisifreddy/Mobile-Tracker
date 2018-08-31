package technology.nine.mobile_tracker;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import technology.nine.mobile_tracker.adapters.CallLogsRecyclerAdapter;
import technology.nine.mobile_tracker.data.CallLogsDBHelper;
import technology.nine.mobile_tracker.model.UserData;

public class MainActivity extends AppCompatActivity {
    CallLogsDBHelper helper;
    RecyclerView recyclerView;
    CallLogsRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<UserData> userData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        permissions();
        helper = new CallLogsDBHelper(this);
        userData = helper.getAllCallLogs();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        adapter = new CallLogsRecyclerAdapter(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.addAll(userData);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public boolean permissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE) + checkSelfPermission(Manifest.permission.READ_CALL_LOG) + checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                }
            }
        }
    }

}
