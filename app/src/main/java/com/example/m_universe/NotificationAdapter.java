package com.example.m_universe;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notifications;
    private Context context;

    public NotificationAdapter(List<Notification> notifications, Context context) {
        this.notifications = notifications;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.actionTypeTextView.setText(notification.getActionType());

        // Format the timestamp
        long timestamp = notification.getTimestamp();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String formattedTimestamp = sdf.format(new Date(timestamp));
        holder.timestampTextView.setText(formattedTimestamp);

        // Set the notification icon based on the actionType
        switch (notification.getActionType()) {
            case "like":
                holder.iconImageView.setImageResource(R.drawable.ic_like);
                holder.messageTextView.setText(notification.getMessage());
                break;
            case "comment":
                holder.iconImageView.setImageResource(R.drawable.ic_comment);
                holder.messageTextView.setText(notification.getMessage());
                break;
            case "repost":
                holder.iconImageView.setImageResource(R.drawable.ic_repost);
                holder.messageTextView.setText(notification.getMessage());
                break;
            case "share":
                holder.iconImageView.setImageResource(R.drawable.ic_share);
                holder.messageTextView.setText(notification.getMessage());
                break;
        }

        // Set the user image
        String userId = notification.getUserId();
        // Fetch the user image using the userId and load it into the ImageView
        // You can use Glide or Picasso for this
        // Example using Glide:
//        String userImageUrl = "your_user_image_url_here" + userId;
//        Glide.with(context).load(userImageUrl).into(holder.userImageView);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView actionTypeTextView, messageTextView, timestampTextView;
        ImageView userImageView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.iconImageView);
            actionTypeTextView = itemView.findViewById(R.id.typeTextView);
            messageTextView = itemView.findViewById(R.id.messageTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            //userImageView = itemView.findViewById(R.id.userImageView);
        }
    }
}