package technology.nine.mobile_tracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.model.CallLogs;
import technology.nine.mobile_tracker.model.SmsLogs;

public class ChatConversationRecyclerAdapter extends RecyclerView.Adapter<ChatConversationRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<SmsLogs> smsLogs;

    public ChatConversationRecyclerAdapter(Context context) {
        this.context = context;
        smsLogs = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        String type = smsLogs.get(i).getMessageType();
        try{
            if (type.equals("Received")) {
                holder.txtMsgFrom.setText(smsLogs.get(i).getMessageBody());
                holder.dateMsgFrom.setText(smsLogs.get(i).getDate());
                holder.timeMsgFrom.setText(smsLogs.get(i).getTime());
                holder.msgFrom.setVisibility(View.VISIBLE);
                holder.msgYou.setVisibility(View.GONE);
            }else {
                holder.lblMsgYou.setText(R.string.you);
                holder.txtMsgYou.setText(smsLogs.get(i).getMessageBody());
                holder.dateMsgYou.setText(smsLogs.get(i).getDate());
                holder.timeMsgYou.setText(smsLogs.get(i).getTime());
                holder.msgFrom.setVisibility(View.GONE);
                holder.msgYou.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return smsLogs.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LinearLayout msgFrom, msgYou;
        TextView txtMsgYou, lblMsgYou, timeMsgYou, txtMsgFrom, timeMsgFrom, dateMsgFrom, dateMsgYou;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtMsgYou = itemView.findViewById(R.id.txtMsgYou);
            lblMsgYou = itemView.findViewById(R.id.lblMsgYou);
            timeMsgYou = itemView.findViewById(R.id.timeMsgYou);
            timeMsgFrom = itemView.findViewById(R.id.timeMsgFrom);
            txtMsgFrom = itemView.findViewById(R.id.txtMsgFrom);
            msgFrom = itemView.findViewById(R.id.msgFrom);
            msgYou = itemView.findViewById(R.id.msgYou);
            dateMsgFrom = itemView.findViewById(R.id.dateMsgFrom);
            dateMsgYou = itemView.findViewById(R.id.dateMsgYou);
        }
    }
    public void add(SmsLogs r) {
        smsLogs.add(r);
        notifyItemInserted(smsLogs.size() - 1);
    }

    public void addAll(List<SmsLogs> callLog) {
        for (SmsLogs result : callLog) {
            add(result);
        }

    }

}
