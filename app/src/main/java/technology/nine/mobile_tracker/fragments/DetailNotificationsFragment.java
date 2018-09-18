package technology.nine.mobile_tracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.adapters.NotificationConversationRecyclerAdapter;
import technology.nine.mobile_tracker.data.LogsDBHelper;
import technology.nine.mobile_tracker.model.Notifications;
import technology.nine.mobile_tracker.utils.OnFragmentInteractionListener;
import technology.nine.mobile_tracker.utils.SimpleDividerItemDecoration;

public class DetailNotificationsFragment extends Fragment {
    View view;
    LogsDBHelper helper;
    RecyclerView recyclerView;
    NotificationConversationRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<Notifications> notifications = new ArrayList<>();
    private String appName;
    private OnFragmentInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
           appName = getArguments().getString("AppName");

        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_layout, container, false);
        if (listener != null) {
            listener.onFragmentInteraction(appName, true);
        }
        recyclerView = view.findViewById(R.id.recycler_view);
        fetch(getContext());
        return view;
    }
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_settings).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        fetch(getContext());
    }

    private void fetch(Context context) {
        helper = new LogsDBHelper(context);
        notifications = helper.getEachPackageNotifications(appName);
        linearLayoutManager = new LinearLayoutManager(context);
        adapter = new NotificationConversationRecyclerAdapter(context);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.addAll(notifications);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
