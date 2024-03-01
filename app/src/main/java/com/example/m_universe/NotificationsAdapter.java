//package com.example.m_universe;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;  // You may need to add the appropriate library for image loading, such as Glide or Picasso
//
//import java.util.List;
//
//
//
//public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {
//
//    private List<Notification> notifications;
//    private Context context;
//
//    public NotificationAdapter(List<Notification> notifications, Context context) {
//        this.notifications = notifications;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.notification_item, parent, false);
//        return new NotificationViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
//        Notification notification = notifications.get(position);
//        holder.typeTextView.setText(notification.getType());
//        holder.timestampTextView.setText(notification.getUid());
//
//
//        // Handle notification types and show appropriate icons and text
//        switch (notification.getType()) {
//            case "like":
//                holder.iconImageView.setImageResource(R.drawable.ic_like);
//                holder.messageTextView.setText(notification.getName() + " liked your post.");
//                break;
//            case "comment":
//                holder.iconImageView.setImageResource(R.drawable.ic_comment);
//                holder.messageTextView.setText(notification.getName() + " commented on your post.");
//                break;
//            case "repost":
//                holder.iconImageView.setImageResource(R.drawable.ic_repost);
//                holder.messageTextView.setText(notification.getName() + " reposted your post.");
//                break;
//            case "share":
//                holder.iconImageView.setImageResource(R.drawable.ic_share);
//                holder.messageTextView.setText(notification.getName() + " shared your post.");
//                break;
//        }
//    }
//    @Override
//    public int getItemCount() {
//        return notifications.size();
//    }
//
//    static class NotificationViewHolder extends RecyclerView.ViewHolder {
//        // ... (previous ViewHolder fields)
//        ImageView iconImageView;
//        TextView messageTextView, typeTextView, timestampTextView;
//        // Constructor for NotificationViewHolder
//        public NotificationViewHolder(@NonNull View itemView) {
//            super(itemView);
//            // Initialize your views here, e.g., iconImageView, messageTextView, etc.
//            // Example:
//             iconImageView = itemView.findViewById(R.id.iconImageView);
//             messageTextView = itemView.findViewById(R.id.messageTextView);
//            typeTextView = itemView.findViewById(R.id.typeTextView);
//            timestampTextView = itemView.findViewById(R.id.timestampTextView);
//        }
//    }
//}
//
