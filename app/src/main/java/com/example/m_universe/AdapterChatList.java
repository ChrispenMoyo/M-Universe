package com.example.m_universe;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class AdapterChatList extends RecyclerView.Adapter<AdapterChatList.Myholder> {

    Context context;
    FirebaseAuth firebaseAuth;
    String uid;

    public AdapterChatList(Context context, List<User> usersList) {
        this.context = context;
        this.usersList = usersList;
        lastMessageMap = new HashMap<>();
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getUid();
    }

    List<User> usersList;
    private HashMap<String, String> lastMessageMap;

    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_chatlist, parent, false);
        return new Myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Myholder holder, final int position) {

        final String hisuid = usersList.get(position).getUid();
        String userimage = usersList.get(position).getImage();
        String username = usersList.get(position).getUname();
        String lastmess = lastMessageMap.get(hisuid);
        holder.name.setText(username);
        holder.block.setImageResource(R.drawable.ic_unblock);


        // if no last message then Hide the layout
        if (lastmess == null || lastmess.equals("default")) {
            holder.lastmessage.setVisibility(View.GONE);
        } else {
            holder.lastmessage.setVisibility(View.VISIBLE);
            holder.lastmessage.setText(lastmess);
        }

        Log.d("AdapterChatList", "onBindViewHolder called for position " + position);
        // loading profile pic of user
        try {
            Glide.with(context)
                    .load(userimage)
                    .placeholder(R.drawable.ic_profile2) // Add a placeholder image
                    .error(R.drawable.ic_home) // Add an error image
                    .into(holder.profile);
        } catch (Exception e) {
            Log.e("AdapterChatList", "Error loading profile image: " + e.getMessage());
        }

        Log.d("AdapterChatList", "onBindViewHolder called for position " + position);

        Log.d("AdapterChatList", "Starting chat activity for user: " + username);

        // redirecting to chat activity on item click
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, chat.class);

            // putting uid of user in extras
            intent.putExtra("uid", hisuid);
            context.startActivity(intent);
        });

    }

    // setting last message sent by users.
    public void setlastMessageMap(String userId, String lastmessage) {
        lastMessageMap.put(userId, lastmessage);
    }

    public void setUsersList(List<User> usersList) {
        this.usersList = usersList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (usersList != null) {
            return usersList.size();
        } else {
            return 0;
        }
    }

    class Myholder extends RecyclerView.ViewHolder {
        ImageView profile, status, block, seen;
        TextView name, lastmessage;

        public Myholder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.profileimage);
            status = itemView.findViewById(R.id.onlinestatus);
            name = itemView.findViewById(R.id.nameonline);
            lastmessage = itemView.findViewById(R.id.lastmessge);
            block = itemView.findViewById(R.id.blocking);
            seen = itemView.findViewById(R.id.seen);
        }
    }
}
