package com.example.m_universe;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationViewHolder extends RecyclerView.ViewHolder {

    private TextView typeTextView;
    private TextView messageTextView;
    private TextView timestampTextView;

    public NotificationViewHolder(@NonNull View itemView) {
        super(itemView);
        typeTextView = itemView.findViewById(R.id.typeTextView);
        messageTextView = itemView.findViewById(R.id.messageTextView);
        timestampTextView = itemView.findViewById(R.id.timestampTextView);
    }

    public void bind(Notification notification) {
        // Set the type of notification (like, comment, share, repost) in typeTextView
        typeTextView.setText(notification.getActionType());

        // Customize the notification text based on the actionType
        String notificationMessage = createNotificationMessage(notification);
        messageTextView.setText(notificationMessage);

        // Set the timestamp in timestampTextView
        long timestamp = notification.getTimestamp();
        String timeAgo = getTimeAgo(timestamp);
        timestampTextView.setText(timeAgo);
    }

    private String createNotificationMessage(Notification notification) {
        // Customize the notification text based on the actionType
        // Example: "chris liked your post."
        if (notification.getUserId() != null) {
            return notification.getUserId() + " " + getActionText(notification.getActionType()) + " your post.";
        } else {
            return "Unknown user performed an action on your post.";
        }

    }

    private String getActionText(String actionType) {
        // Add more cases for different action types if needed
        if (actionType != null) {
            switch (actionType) {
                case "like":
                    return "liked";
                case "comment":
                    return "commented on";
                case "share":
                    return "shared";
                case "repost":
                    return "reposted";
                default:
                    return "performed an action on";
            }
        } else {
            // Handle the case where actionType is null
            return "performed an action on";
        }
    }


    private String getTimeAgo(long timestamp) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - timestamp;

        // Use DateUtils to get the "time ago" format
        return DateUtils.getRelativeTimeSpanString(timestamp, currentTime, DateUtils.MINUTE_IN_MILLIS).toString();
    }
}
