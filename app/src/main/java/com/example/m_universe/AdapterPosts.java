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

    

    List<ModelPost> modelPosts;

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
        post.setPid(modelPosts.get(position).getPtime());

        if (ptime != null && !ptime.isEmpty())
        {
            Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
            calendar.setTimeInMillis(Long.parseLong(ptime));
            String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
            holder.uname.setText(nameh);
//        holder.title.setText(titlee);
            holder.description.setText(descri);
            holder.time.setText(timedate);
            holder.like.setText(plike);
            holder.comments.setText(comm);
            String postid = liekeref.getRef().getKey();
            DatabaseReference postref = FirebaseDatabase.getInstance().getReference().child("Posts").child(postid);
            holder.setLikes(position, postid, holder.like, holder.likebtn);
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
//        holder.likebtn.setOnClickListener(v -> {
//            final int plike1 = Integer.parseInt(modelPosts.get(position).getPlike());
//            mprocesslike = true;
//            final String postid = modelPosts.get(position).getPtime();
//            liekeref.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    if (mprocesslike) {
//                        if (dataSnapshot.child(postid).hasChild(myuid)) {
//                            // User has already liked this post, so unlike it
//                            postref.child(postid).child("plike").runTransaction(new Transaction.Handler() {
//                                @NonNull
//                                @Override
//                                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                                    // Decrement the like count
//                                    Object currentLikes = mutableData.getValue(Long.class);
//                                    if (currentLikes != null) {
//                                        mutableData.setValue((Long) currentLikes - 1);
//                                    }
//                                    return Transaction.success(mutableData);
//                                }
//
//                                @Override
//                                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//                                    if (b) {
//                                        // Update the like button to grey color
//                                        holder.likebtn.setImageResource(R.drawable.ic_like2);
//                                        holder.like.setText(String.valueOf(plike1 - 1));
//                                    }
//                                }
//                            });
//                            liekeref.child(postid).child(myuid).removeValue();
//                        } else {
//                            // User has not yet liked this post, so like it
//                            postref.child(postid).child("plike").runTransaction(new Transaction.Handler() {
//                                @NonNull
//                                @Override
//                                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//                                    // Increment the like count
//                                    Object currentLikes = mutableData.getValue(Long.class);
//                                    if (currentLikes != null) {
//                                        mutableData.setValue((Long) currentLikes + 1);
//                                    }
//                                    return Transaction.success(mutableData);
//                                }
//
//                                @Override
//                                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
//                                    if (b) {
//                                        // Update the like button to blue color
//                                        holder.likebtn.setImageResource(R.drawable.ic_like);
//                                        holder.like.setText(String.valueOf(plike1 + 1));
//                                    }
//                                }
//                            });
//                        }
//                        mprocesslike = false;
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        });

        //share button
        //like button
        holder.likebtn.setOnClickListener(v -> likepost(pid, String.valueOf(plike)));
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
            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postId);
            postRef.child("pshare").runTransaction(new Transaction.Handler() {
                @NonNull
                @Override
                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                    Long currentShareCount = mutableData.getValue(Long.class);
                    if (currentShareCount == null) {
                        mutableData.setValue(1L);
                    } else {
                        mutableData.setValue(currentShareCount + 1);
                    }
                    return Transaction.success(mutableData);
                }

                @Override
                public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                    if (databaseError != null) {
                        Log.e("Firebase", "Failed to increment share count", databaseError.toException());
                    }
                }
            });
        });

        holder.more.setOnClickListener(v -> showMoreOptions(holder.more, myuid, ptime));

        //comment button
        holder.comment.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailsActivity.class);
            intent.putExtra("pid", ptime);
            context.startActivity(intent);
        });
    }

    private void likepost(String postId, String plike) {

        mlike = true;
        final DatabaseReference liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        final DatabaseReference postref = FirebaseDatabase.getInstance().getReference().child("Posts");
        liekeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (mlike) {
                    if (dataSnapshot.child(postId).hasChild(myuid)) {
                        postref.child(postId).child("plike").setValue(plike.toString());
                        postref.child(postId).child("plike").setValue("" + (Integer.parseInt(plike) - 1));
                        liekeref.child(postId).child(myuid).removeValue();
                        mlike = false;

                    } else {
                        postref.child(postId).child("plike").setValue("" + (Integer.parseInt(plike) + 1));
                        liekeref.child(postId).child(myuid).setValue("Liked");
                        mlike = false;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void showMoreOptions(ImageButton more, String uid, final String pid) {
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
                Toast.makeText(context, "About Account selected for post: " + pid, Toast.LENGTH_SHORT).show();
            } else if (item.getItemId() == 2) {
                // Handle Report option
                Toast.makeText(context, "Report selected for post: " + pid, Toast.LENGTH_SHORT).show();
            }

            return false;
        });

        popupMenu.show();
    }

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



    private void setLikes(final int position, final String postid, final TextView like, final ImageView likebtn) {
        liekeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postid).hasChild(myuid)) {
                    likebtn.setImageResource(R.drawable.ic_like);
                } else {
                    likebtn.setImageResource(R.drawable.ic_like2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String plike1 = dataSnapshot.child(postid).child("plike").getValue(String.class);
                like.setText(plike1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateLikeToLong(final String postid) {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child("Posts").child(postid);
        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String plikeStr = dataSnapshot.child("plike").getValue(String.class);
                Long plikeLong = Long.parseLong(plikeStr);
                postRef.child("plike").setValue(plikeLong);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return modelPosts.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        ImageView picture, image;
        TextView uname, time, title, description, like, comments, shares, reposts;
        ImageButton more;
        ImageView likebtn, comment, repost_btn, share_btn;
        LinearLayout profile;

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
        }

        private void setLikes(final int position, final String postid, final TextView like, final ImageView likebtn) {
            liekeref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(postid).hasChild(myuid)) {
                        likebtn.setImageResource(R.drawable.ic_like);
                    } else {
                        likebtn.setImageResource(R.drawable.ic_like2);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            postref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String plike1 = dataSnapshot.child(postid).child("plike").getValue(String.class);
                    like.setText(String.valueOf(plike1));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


    }

}

