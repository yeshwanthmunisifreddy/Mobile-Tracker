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
import java.util.Objects;


import technology.nine.mobile_tracker.MainActivity;
import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.adapters.ChatConversationRecyclerAdapter;
import technology.nine.mobile_tracker.data.LogsDBHelper;
import technology.nine.mobile_tracker.model.SmsLogs;
import technology.nine.mobile_tracker.utils.OnFragmentInteractionListener;

public class ChatFragment extends Fragment {
    LogsDBHelper helper;
    RecyclerView recyclerView;
    ChatConversationRecyclerAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    List<SmsLogs> smsLogs = new ArrayList<>();
    String number;
    View view;
    private OnFragmentInteractionListener listener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            number = getArguments().getString("Number");


        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.recycler_layout, container, false);
        if (listener != null) {
            listener.onFragmentInteraction(number, true);
        }
        recyclerView = view.findViewById(R.id.recycler_view);
        MainActivity mainActivity = (MainActivity) getActivity();
        assert mainActivity != null;
        Objects.requireNonNull(mainActivity.getSupportActionBar()).setTitle(number);
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
        smsLogs = helper.getAllEachNumber(number);
        linearLayoutManager = new LinearLayoutManager(context);
        adapter = new ChatConversationRecyclerAdapter(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.addAll(smsLogs);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
