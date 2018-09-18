package technology.nine.mobile_tracker.adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import technology.nine.mobile_tracker.R;
import technology.nine.mobile_tracker.model.Notifications;
import technology.nine.mobile_tracker.utils.DbBitmapUtility;

public class NotificationConversationRecyclerAdapter extends RecyclerView.Adapter<NotificationConversationRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<Notifications> notifications;

    public NotificationConversationRecyclerAdapter(Context context) {
        this.context = context;
        notifications = new ArrayList<>();

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_conversation_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int i) {
        byte[] bigIcon = notifications.get(i).getBigIcon();
        byte[] extraPicture = notifications.get(i).getExtraPicture();
        String  packageName = notifications.get(i).getPackageName();
        String  date  = notifications.get(i).getDate();
        //
        Date d1 = new Date(Long.parseLong(date));
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        DateFormat timeFormat = new SimpleDateFormat(" hh:mm a ");
        String mDate = dateFormat.format(d1);
        String mTime = timeFormat.format(d1);
        try
        {
            Drawable icon = context.getPackageManager().getApplicationIcon(packageName);
            holder.smallIcon.setImageDrawable(icon);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        holder.app_name.setText(notifications.get(i).getAppName());
        holder.summary .setText(notifications.get(i).getSummary());
        holder.title .setText(notifications.get(i).getTitle());
        holder.text.setText(notifications.get(i).getText());
        String bigText= notifications.get(i).getBigText();
        holder.big_text.setVisibility(View.GONE);
        if (bigText != null){
            visibility(holder);
            holder.big_text.setText(bigText);
        }else {
            holder.expand_more.setVisibility(View.GONE);
        }
        if (bigIcon != null){
            Bitmap largeIcon = DbBitmapUtility.getImage(bigIcon);
            holder.largeIcon.setImageBitmap(largeIcon);
        }
        else {
          holder.largeIcon.setVisibility(View.GONE);
        }
        if(extraPicture != null){
            Bitmap bitmap = DbBitmapUtility.getImage(extraPicture);
            holder.extraPicture.setImageBitmap(bitmap);
        }
        else {
            holder.extraPicture.setVisibility(View.GONE);
        }
        holder.date.setText(mDate);
        holder.time.setText(mTime);



    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView smallIcon, largeIcon, extraPicture;
        TextView app_name, summary, title, text, big_text,date,time;
        LinearLayout linearLayout;
        ToggleButton expand_more;

        MyViewHolder(View itemView) {
            super(itemView);
            smallIcon = itemView.findViewById(R.id.small_icon);
            largeIcon = itemView.findViewById(R.id.large_icon);
            extraPicture = itemView.findViewById(R.id.extra_picture);
            app_name = itemView.findViewById(R.id.app_name);
            summary = itemView.findViewById(R.id.summary);
            title = itemView.findViewById(R.id.title);
            text = itemView.findViewById(R.id.text);
            big_text = itemView.findViewById(R.id.big_text);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            linearLayout = itemView.findViewById(R.id.linear_layout_large_icon);
            expand_more = itemView.findViewById(R.id.expand_more);

        }
    }

    public void add(Notifications r) {
        notifications.add(r);
        notifyItemInserted(notifications.size() - 1);
    }

    public void addAll(List<Notifications> notification) {
        for (Notifications result : notification) {
            add(result);
        }

    }
    private void visibility(final MyViewHolder holder){
        holder.expand_more.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_expand_more_black));
        holder.expand_more.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    holder.expand_more.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_expand_less_black));
                    holder.big_text.setVisibility(View.VISIBLE);
                }else {
                    holder.expand_more.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.ic_expand_more_black));
                    holder.big_text.setVisibility(View.GONE);

                }
            }
        });
    }
}
