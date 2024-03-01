package com.example.m_universe;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

// AdapterChat class is responsible for managing the chat messages displayed in the RecyclerView

public class AdapterChat extends RecyclerView.Adapter<com.example.m_universe.AdapterChat.Myholder> {

    // Constants representing message types for view types in the RecyclerView
    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPR_RIGHT = 1;

    // Constants representing message types for view types in the RecyclerView
    Context context;
    List<ModelChat> list;
    String imageurl;

    // FirebaseUser for the current user
    FirebaseUser firebaseUser;

    // Constructor for the AdapterChat class
    public AdapterChat(Context context, List<ModelChat> list, String imageurl) {
        this.context = context;
        this.list = list;
        this.imageurl = imageurl;
    }

    // Inflating the appropriate layout based on the view type
    @NonNull
    @Override
    public Myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_LEFT) {
            // Inflating the left chat message layout
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_left, parent, false);
            return new Myholder(view);
        } else {
            // Inflating the right chat message layout
            View view = LayoutInflater.from(context).inflate(R.layout.row_chat_right, parent, false);
            return new Myholder(view);
        }
    }

    // Binding data to the ViewHolder based on its position
    @Override
    public void onBindViewHolder(@NonNull Myholder holder, @SuppressLint("RecyclerView") final int position) {
        // Retrieving message details
        String message = list.get(position).getMessage();
        String timeStamp = list.get(position).getTimestamp();
        String type = list.get(position).getType();

        // Converting timestamp to human-readable date and time
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(Long.parseLong(timeStamp));
        String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        // Setting message content, timestamp, and user image
        holder.message.setText(message);
        holder.time.setText(timedate);
        try {
            Glide.with(context).load(imageurl).into(holder.image);
        } catch (Exception e) {
            // Handle image loading exception
        }

        // Displaying either text message or image based on the message type
        if (type.equals("text")) {
            holder.message.setVisibility(View.VISIBLE);
            holder.mimage.setVisibility(View.GONE);
            holder.message.setText(message);
        } else {
            holder.message.setVisibility(View.GONE);
            holder.mimage.setVisibility(View.VISIBLE);
            Glide.with(context).load(message).into(holder.mimage);
        }

        // Handling click on a message to prompt deletion confirmation
        holder.msglayput.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Delete Message");
            builder.setMessage("Are You Sure To Delete This Message");
            builder.setPositiveButton("Delete", (dialog, which) -> deleteMsg(position));
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            builder.create().show();
        });
    }

    // Method to delete a message at a given position
    private void deleteMsg(int position) {
        // Retrieve current user's UID
        final String myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Get the timestamp of the message to be deleted
        String msgtimestmp = list.get(position).getTimestamp();

        // Reference to the 'Chats' node in Firebase Realtime Database
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference().child("Chats");

        // Query to find the message with the specified timestamp
        Query query = dbref.orderByChild("timestamp").equalTo(msgtimestmp);

        // Listen for a single event to perform deletion
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // Check if the current user sent the message
                    if (dataSnapshot1.child("sender").getValue().equals(myuid)) {
                        // any two of below can be used

                        // Delete the message
                        dataSnapshot1.getRef().removeValue();

					/* HashMap<String, Object> hashMap = new HashMap<>();
						hashMap.put("message", "This Message Was Deleted");
						dataSnapshot1.getRef().updateChildren(hashMap);
						Toast.makeText(context,"Message Deleted.....",Toast.LENGTH_LONG).show();
*/
                    } else {
                        // Display a message if the user tries to delete a message they didn't send
                        Toast.makeText(context, "you can delete only your msg....", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if any
            }
        });
    }

    // Get the total number of items in the RecyclerView
    @Override
    public int getItemCount() {
        return list.size();
    }

    // Determine the view type of the item at the specified position
    @Override
    public int getItemViewType(int position) {
        // Get the current user's UID
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // Check if the sender of the message is the current user
        if (list.get(position).getSender().equals(firebaseUser.getUid())) {
            // Right (sent by the current user)
            return MSG_TYPR_RIGHT;
        } else {
            // Left (received from another user)
            return MSG_TYPE_LEFT;
        }
    }

    // ViewHolder class for holding views of each item in the RecyclerView
    class Myholder extends RecyclerView.ViewHolder {

        // Views representing the chat message components
        CircleImageView image;      // User's profile image
        ImageView mimage;           // Image message content
        TextView message, time;      // Text message and timestamp
        TextView isSee;             // "Seen" indicator
        LinearLayout msglayput;     // Layout for the entire message

        // Constructor for the ViewHolder
        public Myholder(@NonNull View itemView) {
            super(itemView);
            // Initialize views from the layout
            image = itemView.findViewById(R.id.profilec);    // Profile image view
            message = itemView.findViewById(R.id.msgc);       // Text message view
            time = itemView.findViewById(R.id.timetv);        // Timestamp view
            isSee = itemView.findViewById(R.id.isSeen);       // "Seen" indicator view
            msglayput = itemView.findViewById(R.id.msglayout); // Message layout view
            mimage = itemView.findViewById(R.id.images);      // Image message view
        }
    }

}
