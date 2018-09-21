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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

import technology.nine.mobile_tracker.utils.MessageContentObserver;
import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.adapters.SmsRecyclerAdapter;
import technology.nine.mobile_tracker.data.LogsDBHelper;
import technology.nine.mobile_tracker.model.SmsLogs;
import technology.nine.mobile_tracker.utils.OnFragmentInteractionListener;

import static technology.nine.mobile_tracker.fragments.NotificationLogFragment.snackbar;

public class MessageLogFragment extends Fragment {
    View view;
    LogsDBHelper helper;
    RecyclerView recyclerView;
    SmsRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<SmsLogs> smsLogs = new ArrayList<>();
    SmsRecyclerAdapter.ClickListener listener;
    ProgressBar progressBar;
    TextView emptyText;
    private OnFragmentInteractionListener mListener;
    //Local broadcast to update the ui from  background service
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            fetch(context);
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_layout, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.recycler_view_progress_bar);
        emptyText = view.findViewById(R.id.recycler_view_empty_text);
        emptyText.setVisibility(View.GONE);
        if (mListener != null) {
            mListener.onFragmentInteraction("Messages", false);
        }
        listener = new SmsRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClicked(String number) {
                ChatFragment chatFragment = new ChatFragment();
                Log.e("Item", "is clicked");
                Bundle args = new Bundle();
                args.putString("Number", number);
                chatFragment.setArguments(args);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.content_frame, chatFragment)
                        .addToBackStack("chatActivity").commit();
            }
        };
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetch(getContext());
            }
        }, 1500);

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
        mListener = null;
    }


    //load Sms  data from database into recycler view
    private void fetch(Context context) {
        helper = new LogsDBHelper(context);
        smsLogs = helper.getAllSMS();
        if (!smsLogs.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            emptyText.setVisibility(View.GONE);
            linearLayoutManager = new LinearLayoutManager(context);
            adapter = new SmsRecyclerAdapter(context, listener);
            recyclerView.setLayoutManager(linearLayoutManager);
            // recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
            adapter.addAll(smsLogs);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            progressBar.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }


    }
}
