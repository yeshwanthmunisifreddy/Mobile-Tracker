package technology.nine.mobile_tracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.model.SmsLogs;

public class SmsRecyclerAdapter extends RecyclerView.Adapter<SmsRecyclerAdapter.MyViewHolder> {
    private List<SmsLogs> smsLogs;
    private Context context;

    public SmsRecyclerAdapter(Context context) {
        this.context = context;
        smsLogs = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sms_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        holder.number.setText(smsLogs.get(i).getPhoneNumber());
        holder.body.setText(smsLogs.get(i).getMessageBody());
        holder.date.setText(smsLogs.get(i).getDate());
    }

    @Override
    public int getItemCount() {
        return smsLogs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number, body, date;

        MyViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            body = itemView.findViewById(R.id.body);
            date = itemView.findViewById(R.id.message_date);
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
