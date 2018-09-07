package technology.nine.mobile_tracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.model.CallLogs;

public class CallLogsRecyclerAdapter extends RecyclerView.Adapter<CallLogsRecyclerAdapter.MyViewHolder> {
    private List<CallLogs> callLogs;
    private Context context;

    public CallLogsRecyclerAdapter(Context context) {
        this.context = context;
        callLogs = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.call_logs_recycler_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        String callType = callLogs.get(i).getCallType();
        String duration = callLogs.get(i).getCallDuration();
        if (callType.equals("1")) {
            holder.callTypeIcon.setImageDrawable(context.getDrawable(R.drawable.ic_incoming_call_icon));
            if (duration.equals("0")) {
                holder.callType.setText(R.string.rejected_call);
                holder.duration.setText(" ");
            } else {
                holder.callType.setText(R.string.incoming_call);
                duration(duration, callType, holder);
            }
        }
        if (callType.equals("2")) {
            holder.callTypeIcon.setImageDrawable(context.getDrawable(R.drawable.ic_outgoing_call_icon));
            if (duration.equals("0")) {
                holder.callType.setText(R.string.didnt_connect);
                holder.duration.setText(" ");
            } else {
                holder.callType.setText(R.string.outgoing_call);
                duration(duration, callType, holder);
            }
        }
        if (callType.equals("3")) {
            holder.callTypeIcon.setImageDrawable(context.getDrawable(R.drawable.ic_missed_call_icon));
            holder.callType.setText(R.string.missed_call);
            holder.duration.setText(" ");

        }
        holder.number.setText(callLogs.get(i).getPhoneNumber());
        holder.date.setText(callLogs.get(i).getCallDate());
        holder.time.setText(callLogs.get(i).getCallTime());
    }

    @Override
    public int getItemCount() {
        return callLogs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        TextView callType;
        ImageView callTypeIcon;
        TextView date;
        TextView time;
        TextView duration;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.phone_number);
            callTypeIcon = itemView.findViewById(R.id.call_type_icon);
            callType = itemView.findViewById(R.id.call_type);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            duration = itemView.findViewById(R.id.duration);
        }
    }

    public void add(CallLogs r) {
        callLogs.add(r);
        notifyItemInserted(callLogs.size() - 1);
    }

    public void addAll(List<CallLogs> callLog) {
        for (CallLogs result : callLog) {
            add(result);
        }

    }

    public void duration(String duration, String callType, MyViewHolder holder) {
        if (!duration.equals("0")) {
            if (!callType.equals("3")) {
                int second = Integer.parseInt(duration);
                if (second > 60) {
                    int seconds = second % 60;
                    int diff = (second - seconds) / 60;
                    holder.duration.setText(diff + "m " + seconds + "sec");
                } else {
                    holder.duration.setText(second + " sec");
                }


            }

        }
    }
}