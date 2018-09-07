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
import java.util.List;
import java.util.Objects;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.adapters.CallLogsRecyclerAdapter;
import technology.nine.mobile_tracker.data.CallLogsDBHelper;
import technology.nine.mobile_tracker.model.CallLogs;
import technology.nine.mobile_tracker.service.CallDetectService;

public class CallLogsFragments extends Fragment {
    View view;
    CallLogsDBHelper helper;
    RecyclerView recyclerView;
    CallLogsRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<CallLogs> callLogs = new ArrayList<>();

    //Local broadcast to update the ui from  background service
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Log.e("OnReceive", "is called");
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
                .registerReceiver(receiver, new IntentFilter(CallDetectService.UPDATE_CALL_LOGS_UI));
    }

    @Override
    public void onResume() {
        super.onResume();
        fetch(getContext());
    }
    //load Call Logs  data from database into recycler view
    private void fetch(Context context) {
        helper = new CallLogsDBHelper(context);
        callLogs = helper.getAllCallLog();
        linearLayoutManager = new LinearLayoutManager(context);
        adapter = new CallLogsRecyclerAdapter(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.addAll(callLogs);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onStop() {
        super.onStop();
        //unRegistration of Local Broadcast
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(receiver);
    }
}