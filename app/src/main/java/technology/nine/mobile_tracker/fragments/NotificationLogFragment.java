package technology.nine.mobile_tracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.adapters.NotificationRecyclerAdapter;
import technology.nine.mobile_tracker.data.LogsDBHelper;
import technology.nine.mobile_tracker.model.NotificationLogs;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification_logs, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_notification_fragment);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.notification_relative_layout);
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
        fetch(getContext());
        notificationAccess();
        return view;
    }

    private void notificationAccess() {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(Objects.requireNonNull(getContext()));
        if (packageNames.isEmpty()) {
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

            Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Go to Notification Access", Snackbar.LENGTH_INDEFINITE)
            .setAction(" Notification Access", snackBArClickListener);
            snackbar.show();
        }
    }

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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        notificationAccess();
    }

    private void fetch(Context context) {
        helper = new LogsDBHelper(context);
        notificationLogs = helper.getAllNotifications();
        linearLayoutManager = new LinearLayoutManager(context);
        adapter = new NotificationRecyclerAdapter(context, listener);
        recyclerView.setLayoutManager(linearLayoutManager);
        // recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        adapter.addAll(notificationLogs);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
