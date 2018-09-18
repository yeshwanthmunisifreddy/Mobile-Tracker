package technology.nine.mobile_tracker.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.model.NotificationLogs;

public class NotificationRecyclerAdapter extends RecyclerView.Adapter<NotificationRecyclerAdapter.MyViewHolder> {
    private List<NotificationLogs> notificationLogs;
    private Context context;
    private ClickListener mListener;

    public interface ClickListener {
        void onItemClicked(String appName);
    }

    public NotificationRecyclerAdapter(Context context, ClickListener listener) {
        this.context = context;
        notificationLogs = new ArrayList<>();
        mListener = listener;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_items, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {
        String packageName = notificationLogs.get(i).getPackageName();
        final String appName = notificationLogs.get(i).getAppName();
        try
        {
            Drawable icon = context.getPackageManager().getApplicationIcon(packageName);
            holder.icon.setImageDrawable(icon);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        String text = notificationLogs.get(i).getText();
        holder.packageName.setText(appName);
        holder.title.setText(notificationLogs.get(i).getTitle());
        if (text != null) {
            holder.text.setText(text);
        } else {
            holder.text.setText("null");
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             mListener.onItemClicked(appName);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationLogs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView packageName, title, text;
        ImageView icon;
        RelativeLayout relativeLayout;

        MyViewHolder(View itemView) {
            super(itemView);
            packageName = itemView.findViewById(R.id.package_name);
            title = itemView.findViewById(R.id.notification_title);
            text = itemView.findViewById(R.id.notification_text);
            icon = itemView.findViewById(R.id.notification_icon);
            relativeLayout = itemView.findViewById(R.id.notification_constraint_layout);
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
