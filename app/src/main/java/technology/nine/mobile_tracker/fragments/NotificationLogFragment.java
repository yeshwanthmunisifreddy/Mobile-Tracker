package technology.nine.mobile_tracker.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.adapters.NotificationRecyclerAdapter;
import technology.nine.mobile_tracker.data.LogsDBHelper;
import technology.nine.mobile_tracker.model.NotificationLogs;
import technology.nine.mobile_tracker.service.CallDetectService;
import technology.nine.mobile_tracker.service.NotificationService;
import technology.nine.mobile_tracker.utils.OnFragmentInteractionListener;

public class NotificationLogFragment extends Fragment {
    View view;
    LogsDBHelper helper;
    RecyclerView recyclerView;
    NotificationRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<NotificationLogs> notificationLogs = new ArrayList<>();
    private NotificationRecyclerAdapter.ClickListener listener;
    private OnFragmentInteractionListener mListener;
    CoordinatorLayout coordinatorLayout;
    public static Snackbar snackbar = null;
    boolean settingValue = false;
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
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification_logs, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_notification_fragment);
        coordinatorLayout = view.findViewById(R.id.notification_relative_layout);
        progressBar = view.findViewById(R.id.progress_bar);
        emptyText = view.findViewById(R.id.empty_text);
        emptyText.setVisibility(View.GONE);
        if (mListener != null) {
            mListener.onFragmentInteraction("Notifications", false);
        }
        listener = new NotificationRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClicked(String appName) {
                DetailNotificationsFragment fragment = new DetailNotificationsFragment();
                Bundle args = new Bundle();
                args.putString("AppName", appName);
                fragment.setArguments(args);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment)
                        .addToBackStack("chatActivity").commit();
            }
        };
         settingValue = true;
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
                .registerReceiver(receiver, new IntentFilter(NotificationService.UPDATE_NOTIFICATIONS_LOGS_UI));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!settingValue){
            fetch(getContext());
        }
        notificationAccess();
         settingValue = false;
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

    private void notificationAccess() {
        Log.e("notificationAccess","is called");
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(Objects.requireNonNull(getContext()));
        if (!packageNames.contains("technology.nine.mobile_tracker")) {
            View.OnClickListener snackBArClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= 22) {
                        startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    } else {
                        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                    }
                }
            };
            snackbar = Snackbar.make(coordinatorLayout, "Go to Notification Access and Allow Permissions", Snackbar.LENGTH_INDEFINITE)
                    .setAction(" Notification Access", snackBArClickListener);
            snackbar.show();

        }
    }


    private void fetch(Context context) {
        helper = new LogsDBHelper(context);
        notificationLogs = helper.getAllNotifications();
        if (!notificationLogs.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            emptyText.setVisibility(View.GONE);
            linearLayoutManager = new LinearLayoutManager(context);
            adapter = new NotificationRecyclerAdapter(context, listener);
            recyclerView.setLayoutManager(linearLayoutManager);
            // recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
            adapter.addAll(notificationLogs);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            progressBar.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }


    }


}
