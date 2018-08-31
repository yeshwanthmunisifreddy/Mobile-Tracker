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
import technology.nine.mobile_tracker.model.UserData;

public class CallLogsRecyclerAdapter extends RecyclerView.Adapter<CallLogsRecyclerAdapter.MyViewHolder> {
    List<UserData> userData;
    Context context;

    public CallLogsRecyclerAdapter(Context context) {
        this.context = context;
        userData = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.call_logs_recycler_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        String number = userData.get(i).getNumber();
        String callType = userData.get(i).getCallType();
        String date = userData.get(i).getDate();
        String time = userData.get(i).getTime();
        holder.number.setText(number);
        if (callType.equals("Incoming call")){
            holder.callTypeIcon.setImageDrawable(context.getDrawable(R.drawable.ic_incoming_call_icon));
        }
        if (callType.equals("Outgoing call")){
            holder.callTypeIcon.setImageDrawable(context.getDrawable(R.drawable.ic_outgoing_call_icon));
        }
        if (callType.equals("Missed call")){
            holder.callTypeIcon.setImageDrawable(context.getDrawable(R.drawable.ic_missed_call_icon));
        }
        holder.callType.setText(callType);
        holder.date.setText(date);
        holder.time.setText(time);

    }

    @Override
    public int getItemCount() {
        return userData.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number;
        TextView callType;
        ImageView callTypeIcon;
        TextView date;
        TextView time;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.phone_number);
            callTypeIcon =itemView.findViewById(R.id.call_type_icon);
            callType = itemView.findViewById(R.id.call_type);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
        }
    }

    public void add(UserData r) {
        userData.add(r);
        notifyItemInserted(userData.size() - 1);
    }
    public  void addAll(List<UserData> userDatabase){
        for (UserData result :userDatabase){
            add(result);
        }

    }
}