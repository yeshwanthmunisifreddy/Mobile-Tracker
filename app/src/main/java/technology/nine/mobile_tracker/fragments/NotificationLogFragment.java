package technology.nine.mobile_tracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

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
    private  NotificationRecyclerAdapter.ClickListener listener;
    private OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_layout, container, false);
        if (mListener != null) {
            mListener.onFragmentInteraction("Notifications",false);
        }
        listener = new NotificationRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClicked(String appName) {
                DetailNotificationsFragment fragment = new DetailNotificationsFragment();
                Bundle args = new Bundle();
                args.putString("AppName",appName );
                fragment.setArguments(args);
                assert getFragmentManager() != null;
                getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment)
                        .addToBackStack("chatActivity").commit();
            }
        };
        recyclerView = view.findViewById(R.id.recycler_view);
        fetch(getContext());
        return view;
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

    private void fetch(Context context) {
        helper = new LogsDBHelper(context);
        notificationLogs = helper.getAllNotifications();
        linearLayoutManager = new LinearLayoutManager(context);
        adapter = new NotificationRecyclerAdapter(context,listener);
        recyclerView.setLayoutManager(linearLayoutManager);
        // recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        adapter.addAll(notificationLogs);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }
}
