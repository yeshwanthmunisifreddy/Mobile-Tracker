package technology.nine.mobile_tracker.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.model.NotificationLogs;
import technology.nine.mobile_tracker.model.SmsLogs;
import technology.nine.mobile_tracker.utils.DbBitmapUtility;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.MyViewHolder> {
    private List<NotificationLogs> notificationLogs;
    private Context context;

    public NotificationRecyclerAdapter(Context context) {
        this.context = context;
        notificationLogs = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_items, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        int i = position;
        String text = notificationLogs.get(i).getText();
        byte[] image = notificationLogs.get(i).getImage();
        if (image != null){
            Bitmap bmp = DbBitmapUtility.getImage(image);
            holder.icon.setImageBitmap(bmp);
        }
        holder.packageName.setText(notificationLogs.get(i).getPackageName());
        holder.title.setText(notificationLogs.get(i).getTitle());
        if (text !=null){
            holder.text.setText(text);
        }
        else {
            holder.text.setText("null");
        }


    }

    @Override
    public int getItemCount() {
        return notificationLogs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView packageName, title, text;
        ImageView icon;
        MyViewHolder(View itemView) {
            super(itemView);
            packageName = itemView.findViewById(R.id.package_name);
            title = itemView.findViewById(R.id.notification_title);
            text = itemView.findViewById(R.id.notification_text);
            icon = itemView.findViewById(R.id.notification_icon);
        }
    }

    public void add(NotificationLogs r) {
        notificationLogs.add(r);
        notifyItemInserted(notificationLogs.size() - 1);
    }

    public void addAll(List<NotificationLogs> notificationLog) {
        for (NotificationLogs result : notificationLog) {
            add(result);
        }

    }
}
