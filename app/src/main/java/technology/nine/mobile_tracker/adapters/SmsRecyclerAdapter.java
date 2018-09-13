package technology.nine.mobile_tracker.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import technology.nine.mobile_tracker.ChatActivity;
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
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final int i = position;
        holder.number.setText(smsLogs.get(i).getPhoneNumber());
        holder.body.setText(smsLogs.get(i).getMessageBody());
        holder.date.setText(smsLogs.get(i).getDate());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            public Activity mContext;

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("Number",smsLogs.get(i).getPhoneNumber());
                context.startActivity(intent);
                mContext = (Activity) holder.itemView.getContext();
                mContext.overridePendingTransition(R.anim.enter, R.anim.exit);
            }
        });

    }

    @Override
    public int getItemCount() {
        return smsLogs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        TextView body;
        TextView date;
        ConstraintLayout constraintLayout;
        MyViewHolder(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
            body = itemView.findViewById(R.id.body);
            date = itemView.findViewById(R.id.message_date);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
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
