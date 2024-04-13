package com.example.m_universe;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog.Builder;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AdapterPosts extends RecyclerView.Adapter<com.example.m_universe.AdapterPosts.MyHolder> {

    Context context;
    static String myuid;
    boolean mlike = false;
    private static DatabaseReference liekeref;
    private static DatabaseReference postref;
    boolean mprocesslike = false;
    private List<ModelPost> posts;

    public AdapterPosts(Context context, List<ModelPost> modelPosts) {
        this.context = context;
        this.modelPosts = modelPosts;
        myuid = (FirebaseAuth.getInstance().getCurrentUser() != null) ? FirebaseAuth.getInstance().getCurrentUser().getUid() : null;
        liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        postref = FirebaseDatabase.getInstance().getReference().child("Posts");
        repostref = FirebaseDatabase.getInstance().getReference().child("Reposts");

    }
    // Add this line to declare the repostref
    private static DatabaseReference repostref;

    

    static List<ModelPost> modelPosts;

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rows_posts, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, @SuppressLint("RecyclerView") final int position) {
        // Get the data of the post
        final String uid = modelPosts.get(position).getUid();
        String nameh = modelPosts.get(position).getUname();
//        final String titlee = modelPosts.get(position).getTitle();
        final String descri = modelPosts.get(position).getDescription();
        final String ptime = modelPosts.get(position).getPtime();
        String dp = modelPosts.get(position).getUdp();
        String plike = modelPosts.get(position).getPlike();
        final String image = modelPosts.get(position).getUimage();
        String email = modelPosts.get(position).getUemail();
        String comm = modelPosts.get(position).getPcomments();
        final String pid = modelPosts.get(position).getPid();
        final ModelPost post = modelPosts.get(position);
//        Long plike = post.getPlike();
//        post.setPlike(plike);

        if (ptime != null && !ptime.isEmpty())
        {
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis(Long.parseLong(ptime));
            String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
            holder.uname.setText(nameh);
//          holder.title.setText(titlee);
            holder.description.setText(descri);
            holder.time.setText(timedate);
            holder.like.setText(String.valueOf(plike));
            //comments count
            holder.comments.setText(comm);
            String postid = liekeref.getRef().getKey();
            DatabaseReference postref = FirebaseDatabase.getInstance().getReference().child("Posts").child(postid);
            //holder.setLikes(0, postid, holder.like, holder.likebtn);

            //shares count
            holder.shares.setText(String.valueOf(modelPosts.get(position).getPshare()));
            //more button
            holder.more.setOnClickListener(v -> showMoreOptions(holder.more, myuid, ptime));
            //comment button
            holder.comment.setOnClickListener(v -> {
                Intent intent = new Intent(context, PostDetailsActivity.class);
                intent.putExtra("pid", ptime);
                context.startActivity(intent);
                // Assuming you have the post author's ID available in your modelPosts list
                String postAuthorId = modelPosts.get(position).getUid();

                // Add this line to send a notification when a like is performed
                sendNotification(myuid, "comment", pid, postAuthorId);
            });
            //repost button
            // Set the repost button click listener
            holder.repost_btn.setOnClickListener(v -> {
                // Check if the user has already reposted the post
                repostref.child(pid).child(myuid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // User has already reposted the post
                            holder.repost_btn.setImageResource(R.drawable.ic_repost);
                            holder.repostedBy.setText("Reposted by " + nameh);
                            holder.repostedBy.setVisibility(View.VISIBLE);
                        } else {
                            // User hasn't reposted the post yet
                            MyHolder myHolder = holder;
                            myHolder.showRepostDialog(pid, nameh);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("Firebase", "Failed to check if user has reposted the post", databaseError.toException());
                    }
                });
            });

            // Hide the "repostedBy" TextView initially
            holder.repostedBy.setVisibility(View.GONE);
            //share button
            holder.share_btn.setOnClickListener(v -> {
                // Create the Intent
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                // Add the post data to the Intent
                String shareBody = "Check this out : " + descri + "\n";
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                // Start the activity
                context.startActivity(intent.createChooser(intent, "Share using ... "));

                // Add the share to the Shares node
                String postId = post.getPid();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference sharesRef = FirebaseDatabase.getInstance().getReference().child("Shares");
                Shares shares = new Shares(postId, userId);
                sharesRef.push().setValue(shares);

                // Increment the share count of the post
                updateShareCount(postId);
                // Assuming you have the post author's ID available in your modelPosts list
                String postAuthorId = modelPosts.get(position).getUid();

                // Add this line to send a notification when a like is performed
                sendNotification(myuid, "share", pid, postAuthorId);
            });


        }

        try {
            Glide.with(context).load(dp).into(holder.picture);
        } catch (Exception e) {

        }
        holder.image.setVisibility(View.VISIBLE);
        try {
            Glide.with(context).load(image).into(holder.image);
        } catch (Exception e) {

        }

        holder.like.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(),  PostLikedByActivity.class);
            intent.putExtra("pid", pid);
            holder.itemView.getContext().startActivity(intent);

        });

        //like button
       // holder.likebtn.setOnClickListener(v -> likepost(pid, plike));

    }

    //for incrementing share count
    private void updateShareCount(String postId) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the current share count as a string
                String currentShareCountString = dataSnapshot.child("pshare").getValue(String.class);

                if (currentShareCountString != null) {
                    // Convert the String to long
                    long currentShareCount = Long.parseLong(currentShareCountString);

                    // Increment the share count
                    long newShareCount = currentShareCount + 1;

                    // Update the new share count to the database as a string
                    reference.child("pshare").setValue(String.valueOf(newShareCount));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to increment share count", databaseError.toException());
            }
        });
    }
    private void likepost(String postId, Long plike) {

        mlike = true;
        final DatabaseReference liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        final DatabaseReference postref = FirebaseDatabase.getInstance().getReference().child("Posts");
        liekeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (mlike) {
                    if (dataSnapshot.child(postId).hasChild(myuid)) {
                        // User has already liked the post
                        postref.child(postId).child("plike").setValue(plike - 1L);
                        liekeref.child(postId).child(myuid).removeValue();
                        mlike = false;


                    } else {
                        // User hasn't liked the post yet
                        postref.child(postId).child("plike").setValue(plike + 1L);
                        liekeref.child(postId).child(myuid).setValue(true);
                        mlike = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //for repost
    private static void repostPost(String postId, String reposterName) {
        // Add the repost to the Reposts node
        DatabaseReference repostsRef = FirebaseDatabase.getInstance().getReference().child("Reposts");
        Repost repost = new Repost(postId, FirebaseAuth.getInstance().getCurrentUser().getUid(), reposterName);
        repostsRef.push().setValue(repost);

        // Increment the repost count of the post
        updateRepostCount(postId);
    }

    private static void updateRepostCount(String postId) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Get the current repost count as a string
                String currentRepostCountString = dataSnapshot.child("p_repost").getValue(String.class);

                if (currentRepostCountString != null) {
                    // Convert the String to long
                    long currentRepostCount = Long.parseLong(currentRepostCountString);

                    // Increment the repost count
                    long newRepostCount = currentRepostCount + 1;

                    // Update the new repost count to the database as a string
                    reference.child("p_repost").setValue(String.valueOf(newRepostCount));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Failed to increment repost count", databaseError.toException());
            }
        });
    }
    //for the options of Show More button
    private void showMoreOptions(ImageButton more, String uname, final String pid) {
        PopupMenu popupMenu = new PopupMenu(context, more, Gravity.END);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Translate");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "About Account");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "Report");

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 0) {
                // Handle Translate option
                createLanguageDialog(pid);
            } else if (item.getItemId() == 1) {
                // Handle About Account option
                navigateToUserProfile(uname);
            } else if (item.getItemId() == 2) {
                // Handle Report option
                Toast.makeText(context, "Report selected for post: " + pid, Toast.LENGTH_SHORT).show();
            }

            return false;
        });

        popupMenu.show();
    }

    //choose the language to translate to
    private void createLanguageDialog(String pid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Translation Language");

        String[] languages = {"English", "Hindi", "Gujarati"};
        int selectedLanguageIndex = -1;

        builder.setSingleChoiceItems(languages, selectedLanguageIndex, (dialog, which) -> {
            String selectedLanguage = languages[which];
            Toast.makeText(context, "Translation to " + selectedLanguage + " for post: " + pid, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //navigate to user's profile
    private void navigateToUserProfile(String uname) {
        // Create an Intent to start the Profile activity
        Intent intent = new Intent(context, userProfileActivity.class);
        // Pass the UID of the user whose profile you want to display
        intent.putExtra("uname", uname);
        // Start the activity
        context.startActivity(intent);
    }


    //sending notifications
    //sending notifications
    private void sendNotification(String receiverId, String actionType, String postId, String message) {
        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference("Notifications");

        String notificationId = notificationsRef.push().getKey();
        long timestamp = System.currentTimeMillis();

        Notification notification = new Notification(receiverId, actionType, postId, timestamp, message);

        if (notificationId != null) {
            notificationsRef.child(receiverId).child(notificationId).setValue(notification)
                    .addOnSuccessListener(aVoid -> Log.d("Notification", "Notification sent successfully"))
                    .addOnFailureListener(e -> Log.e("Notification", "Error sending notification", e));
        }
    }






    @Override
    public int getItemCount() {
        return modelPosts.size();
    }

    public void updatePosts(List<ModelPost> posts) {
        this.posts = posts;
        notifyDataSetChanged();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        ImageView picture, image;
        TextView uname, time, title, description, like, comments, shares,repostedBy, reposts;
        ImageButton more;
        ImageView likebtn, comment, repost_btn, share_btn;
        LinearLayout profile;
        private AlertDialog repostDialog;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            picture = itemView.findViewById(R.id.picturetv);
            image = itemView.findViewById(R.id.pimagetv);
            uname = itemView.findViewById(R.id.unametv);
            time = itemView.findViewById(R.id.utimetv);
            more = itemView.findViewById(R.id.morebtn);
            //title = itemView.findViewById(R.id.ptitletv);
            description = itemView.findViewById(R.id.descript);
            like = itemView.findViewById(R.id.likeCount);
            shares = itemView.findViewById(R.id.shareCount);
            reposts = itemView.findViewById(R.id.repostCount);
            comments = itemView.findViewById(R.id.commentCount);
            likebtn = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            profile = itemView.findViewById(R.id.profilelayout);
            repost_btn = itemView.findViewById(R.id.repost);
            share_btn = itemView.findViewById(R.id.share);
            repostedBy = itemView.findViewById(R.id.repostedBy);

            // Initialize the repost dialog
            AlertDialog.Builder builder = new Builder(itemView.getContext());
            builder.setMessage("Do you want to REPOST?");
            builder.setPositiveButton("Repost", (dialog, which) -> {
                // If the user selects "Repost", perform the repost action
                //repostPost(pid, nameh);
//                repostedBy.setText("Reposted by ");
//                repostedBy.setVisibility(View.VISIBLE);
            });
            builder.setNegativeButton("Cancel", null);
            repostDialog = builder.create();

        }

        public void showRepostDialog(String pid, String nameh) {
            repostDialog.show();
        }

//        private void setLikes(final int position, final String postid, final TextView like, final ImageView likebtn) {
//            liekeref.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.child(postid).hasChild(myuid)) {
//                        likebtn.setImageResource(R.drawable.ic_like);
//                    } else {
//                        likebtn.setImageResource(R.drawable.ic_like2);
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//
//            postref.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    String plike1 = dataSnapshot.child(postid).child("plike").getValue(String.class);
//                    Long plikeLong = Long.parseLong(plike1);
//                    modelPosts.get(position).setPlike(plikeLong);
//                    like.setText(plike1.toString());
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }


    }

}

