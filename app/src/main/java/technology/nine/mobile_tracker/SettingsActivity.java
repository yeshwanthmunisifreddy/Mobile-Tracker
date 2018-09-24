package technology.nine.mobile_tracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import technology.nine.mobile_tracker.utils.OnFragmentInteractionListener;

public class SettingsActivity extends Fragment {
    View view;
    private OnFragmentInteractionListener listener;

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
        view = inflater.inflate(R.layout.activity_settings, container, false);
        if (listener != null) {
            listener.onFragmentInteraction("Settings", false);
        }
        return view;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

}
