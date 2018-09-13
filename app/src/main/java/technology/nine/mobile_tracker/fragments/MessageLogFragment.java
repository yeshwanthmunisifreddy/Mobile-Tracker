package technology.nine.mobile_tracker.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import technology.nine.mobile_tracker.MessageContentObserver;
import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.adapters.SmsRecyclerAdapter;
import technology.nine.mobile_tracker.data.LogsDBHelper;
import technology.nine.mobile_tracker.model.SmsLog;
import technology.nine.mobile_tracker.model.SmsLogs;

public class MessageLogFragment extends Fragment {
    View view;
    LogsDBHelper helper;
    RecyclerView recyclerView;
    SmsRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<SmsLogs> smsLogs = new ArrayList<>();
    //Local broadcast to update the ui from  background service
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("UpdateBroadcast","is called");
            fetch(context);
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_call_log, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        fetch(getContext());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        //registration of Local Broadcast
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext()))
                .registerReceiver(receiver, new IntentFilter(MessageContentObserver.UPDATE_SMS_LOGS_UI));
    }

    @Override
    public void onResume() {
        super.onResume();
        fetch(getContext());
    }

    @Override
    public void onStop() {
        super.onStop();
        //unRegistration of Local Broadcast
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(receiver);
    }

    //load Sms  data from database into recycler view
    private void fetch(Context context) {
        helper = new LogsDBHelper(context);
        smsLogs = helper.getAllSMS();
       linearLayoutManager = new LinearLayoutManager(context);
        adapter = new SmsRecyclerAdapter(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.addAll(smsLogs);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
