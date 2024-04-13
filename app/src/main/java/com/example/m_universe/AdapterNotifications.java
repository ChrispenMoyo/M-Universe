package com.example.m_universe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterNotifications extends RecyclerView.Adapter<AdapterNotifications.NotificationViewHolder> {

    List<Notification> notifications = new ArrayList<>();
    private OnItemClickListener listener;

    public AdapterNotifications(List<Notification> notifications, OnItemClickListener listener) {
        this.notifications = notifications;
        this.listener = listener;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view, listener, AdapterNotifications.this);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        holder.bind(notifications.get(position));
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Notification notification);
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {

        public NotificationViewHolder(View itemView, final OnItemClickListener listener, final AdapterNotifications adapterNotifications) {
            super(itemView);

            // Initialize your views here

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(adapterNotifications.notifications.get(position));
                    }
                }
            });
        }

        public void bind(Notification notification) {
            // Implement your binding logic here
        }
    }
}