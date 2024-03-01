package com.example.m_universe;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;
import java.util.List;

// Adapter class for managing the RecyclerView in the ChatList activity
public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.Myholder> {

    // Context of the activity
    Context context;
    // Firebase authentication instance
    FirebaseAuth firebaseAuth;

    // User ID of the current user
    String uid;

    // List to store user data
    List<User> usersList;

    // HashMap to store the last message for each user
    private HashMap<String, String> lastMessageMap;

    // Constructor for the AdapterChatList class
    public AdapterChatList(Context context, List<User> users) {
        this.context = context;
        this.usersList = users;
        lastMessageMap = new HashMap<>();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
    }

    // Method to create a new ViewHolder instance
    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflating the layout for each item in the RecyclerView
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, parent, false);
        return new Myholder(view);
    }

    // Method to bind data to the ViewHolder
    @Override
    public void onBindViewHolder(@NonNull Myholder holder, final int position) {
        // Retrieving data for the current user
        final String hisuid = usersList.get(position).getUserId();
        //String userimage = usersList.get(position).getImage();
        String username = usersList.get(position).getUname();
        String lastmess = lastMessageMap.get(hisuid);

        // Setting user name and default block icon
        holder.name.setText(username);
        holder.block.setImageResource(R.drawable.ic_unblock);

        // if no last message then Hide the layout
        if (lastmess == null || lastmess.equals("default")) {
            holder.lastmessage.setVisibility(View.GONE);
        } else {
            holder.lastmessage.setVisibility(View.VISIBLE);
            holder.lastmessage.setText(lastmess);
        }
//        try {
//            // loading profile pic of user
//            Glide.with(context).load(userimage).into(holder.profile);
//        } catch (Exception e) {
//
//        }

        // redirecting to chat activity on item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, chat.class);

            // putting uid of user in extras
            intent.putExtra("uid", hisuid);
            context.startActivity(intent);
        });

    }

    // Method to set the last message for a specific user
    public void setlastMessageMap(String userId, String lastmessage) {
        lastMessageMap.put(userId, lastmessage);
    }

    // Method to get the total number of items in the RecyclerView
    @Override
    public int getItemCount() {
        return usersList.size();
    }

    // ViewHolder class to hold the views for each item in the RecyclerView
    static class Myholder extends RecyclerView.ViewHolder {
        ImageView profile, status, block, seen;
        TextView name, lastmessage;

        // Constructor for the ViewHolder class
        public Myholder(@NonNull View itemView) {
            super(itemView);

            // Initializing views
            profile = itemView.findViewById(R.id.profileimage);
            status = itemView.findViewById(R.id.onlinestatus);
            name = itemView.findViewById(R.id.nameonline);
            lastmessage = itemView.findViewById(R.id.lastmessge);
            block = itemView.findViewById(R.id.blocking);
            seen = itemView.findViewById(R.id.seen);
        }
    }
}
