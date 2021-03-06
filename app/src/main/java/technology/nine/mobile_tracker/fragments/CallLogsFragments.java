package technology.nine.mobile_tracker.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.adapters.CallLogsRecyclerAdapter;
import technology.nine.mobile_tracker.data.LogsDBHelper;
import technology.nine.mobile_tracker.model.CallLogs;
import technology.nine.mobile_tracker.service.CallDetectService;
import technology.nine.mobile_tracker.utils.OnFragmentInteractionListener;

import static technology.nine.mobile_tracker.fragments.NotificationLogFragment.snackbar;

public class CallLogsFragments extends Fragment {
    View view;
    LogsDBHelper helper;
    RecyclerView recyclerView;
    CallLogsRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<CallLogs> callLogs = new ArrayList<>();
    private OnFragmentInteractionListener listener;
    ProgressBar progressBar;
    TextView emptyText;
    //Local broadcast to update the ui from  background service
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Log.e("OnReceive", "is called");
            fetch(context);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_layout, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.recycler_view_progress_bar);
        emptyText = view.findViewById(R.id.recycler_view_empty_text);
        emptyText.setVisibility(View.GONE);
        if (listener != null) {
            listener.onFragmentInteraction("Calls", false);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetch(getContext());
            }
        }, 1000);
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



    }

    @Override
    public void onStop() {
        super.onStop();
        //unRegistration of Local Broadcast
        LocalBroadcastManager.getInstance(Objects.requireNonNull(getContext())).unregisterReceiver(receiver);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    //load Call Logs  data from database into recycler view
    private void fetch(Context context) {
        helper = new LogsDBHelper(context);
        callLogs = helper.getAllCallLog();
        if (!callLogs.isEmpty()) {
            emptyText.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            linearLayoutManager = new LinearLayoutManager(context);
            adapter = new CallLogsRecyclerAdapter(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter.addAll(callLogs);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            emptyText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }


    }


}
