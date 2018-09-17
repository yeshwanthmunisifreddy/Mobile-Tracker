package technology.nine.mobile_tracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.model.SmsLogs;

public class SmsRecyclerAdapter extends RecyclerView.Adapter<SmsRecyclerAdapter.MyViewHolder> {
    private List<SmsLogs> smsLogs;
    private Context context;
    private ClickListener mListener;

    public interface ClickListener {
        void onItemClicked(String number);
    }


    public SmsRecyclerAdapter(Context context,ClickListener listener) {
        this.context = context;
        smsLogs = new ArrayList<>();
        mListener = listener;


    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final String number = smsLogs.get(position).getPhoneNumber();
        holder.number.setText(number);
        holder.body.setText(smsLogs.get(position).getMessageBody());
        holder.date.setText(smsLogs.get(position).getDate());
        holder.time.setText(smsLogs.get(position).getTime());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(number);
            }
        });


    }

    @Override
    public int getItemCount() {
        return smsLogs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number, body, date, time;
        RelativeLayout relativeLayout;

        MyViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            body = itemView.findViewById(R.id.body);
            date = itemView.findViewById(R.id.message_date);
            time = itemView.findViewById(R.id.message_time);
            relativeLayout = itemView.findViewById(R.id.sms_relative_layout);
        }
    }

    public void add(SmsLogs r) {
        smsLogs.add(r);
        notifyItemInserted(smsLogs.size() - 1);
    }

    public void addAll(List<SmsLogs> smsLog) {
        for (SmsLogs result : smsLog) {
            add(result);
        }

    }

}
