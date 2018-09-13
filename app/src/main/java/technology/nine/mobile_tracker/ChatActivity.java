package technology.nine.mobile_tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import technology.nine.mobile_tracker.adapters.ChatConversationRecyclerAdapter;
import technology.nine.mobile_tracker.data.LogsDBHelper;
import technology.nine.mobile_tracker.model.SmsLogs;
import technology.nine.mobile_tracker.utils.MessageContentObserver;

import static java.security.AccessController.getContext;

public class ChatActivity extends AppCompatActivity {
    LogsDBHelper helper;
    RecyclerView recyclerView;
    ChatConversationRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<SmsLogs> smsLogs = new ArrayList<>();
    String number;
    //Local broadcast to update the ui from  background service
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fetch(getApplicationContext());
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_chat);
        recyclerView = findViewById(R.id.chat_recycler_view);
        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark));
        //Read the extra data from intent
        Intent intent = getIntent();
        number = intent.getStringExtra("Number");
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(number);
        fetch(getApplicationContext());

    }
    @Override
    public void onStart() {
        super.onStart();
        //registration of Local Broadcast
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext()))
                .registerReceiver(receiver, new IntentFilter(MessageContentObserver.UPDATE_SMS_LOGS_UI));
    }

    @Override
    public void onResume() {
        super.onResume();
        fetch(getApplicationContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        //unRegistration of Local Broadcast
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getApplicationContext())).unregisterReceiver(receiver);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }
    private  void fetch(Context context){
        helper = new LogsDBHelper(context);
        smsLogs = helper.getAllEachNumber(number);
        linearLayoutManager = new LinearLayoutManager(context);
        adapter = new ChatConversationRecyclerAdapter(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.addAll(smsLogs);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}

