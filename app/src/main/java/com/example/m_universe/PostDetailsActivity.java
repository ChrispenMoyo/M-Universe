package com.example.m_universe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PostDetailsActivity extends AppCompatActivity {


    String hisuid, ptime, myuid, myname, myemail, mydp, uimage, postId, plike, hisdp, hisname;
    ImageView picture, image;
    TextView name, time, title, description, like, tcomment;
    ImageButton more;

    LinearLayout profile;
    EditText comment;
    ImageButton sendb;
    RecyclerView recyclerView;
    List<ModelComment> commentList;
    AdapterComment adapterComment;
    ImageView imagep, likebtn, share;
    boolean mlike = false;
    ActionBar actionBar;
    ProgressDialog progressDialog;
    private static DatabaseReference liekeref;
    private static DatabaseReference postref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);
//        actionBar = getSupportActionBar();
//        actionBar.setTitle("Post Details");
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);
        postId = getIntent().getStringExtra("pid");
        recyclerView = findViewById(R.id.recyclecomment);
        picture = findViewById(R.id.picturetv);
        image = findViewById(R.id.pimagetv);
        name = findViewById(R.id.unametv);
        time = findViewById(R.id.utimetv);
        more = findViewById(R.id.morebtn);
       // title = findViewById(R.id.ptitleco);
        myemail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        myuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        description = findViewById(R.id.descript);
        tcomment = findViewById(R.id.commentCount);
        like = findViewById(R.id.likeCount);
        likebtn = findViewById(R.id.like);
        comment = findViewById(R.id.typecommet);
        sendb = findViewById(R.id.sendcomment);
        imagep = findViewById(R.id.commentimge);
        share = findViewById(R.id.share);
        profile = findViewById(R.id.profilelayout);
        progressDialog = new ProgressDialog(this);

        liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        postref = FirebaseDatabase.getInstance().getReference().child("Posts");


        loadPostInfo();

        loadUserInfo();
        setLikes(0, postId, like, likebtn);

       // actionBar.setSubtitle("SignedInAs:" + myemail);
        loadComments();
        sendb.setOnClickListener(v -> postComment());
        likebtn.setOnClickListener(v -> likepost());
        like.setOnClickListener(v -> {
            Intent intent = new Intent(PostDetailsActivity.this, PostLikedByActivity.class);
            intent.putExtra("pid", postId);
            startActivity(intent);
        });
    }

    private void loadComments() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        commentList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    ModelComment modelComment = dataSnapshot1.getValue(ModelComment.class);
                    commentList.add(modelComment);
                    adapterComment = new AdapterComment(getApplicationContext(), commentList, myuid, postId);
                    recyclerView.setAdapter(adapterComment);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

    private void likepost() {

        mlike = true;
        final DatabaseReference liekeref = FirebaseDatabase.getInstance().getReference().child("Likes");
        final DatabaseReference postref = FirebaseDatabase.getInstance().getReference().child("Posts");
        liekeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (mlike) {
                    if (dataSnapshot.child(postId).hasChild(myuid)) {
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

    private void postComment() {
        progressDialog.setMessage("Adding Comment");

        final String commentss = comment.getText().toString().trim();
        if (TextUtils.isEmpty(commentss)) {
            Toast.makeText(PostDetailsActivity.this, "Empty comment", Toast.LENGTH_LONG).show();
            return;
        }
        progressDialog.show();
        String timestamp = String.valueOf(System.currentTimeMillis());
        DatabaseReference datarf = FirebaseDatabase.getInstance().getReference("Posts").child(postId).child("Comments");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("cId", timestamp);
        hashMap.put("comment", commentss);
        hashMap.put("ptime", timestamp);
        hashMap.put("uid", myuid);
        hashMap.put("uemail", myemail);
        hashMap.put("udp", mydp);
        hashMap.put("uname", myname);
        datarf.child(timestamp).setValue(hashMap).addOnSuccessListener(aVoid -> {
            progressDialog.dismiss();
            Toast.makeText(PostDetailsActivity.this, "Added", Toast.LENGTH_LONG).show();
            comment.setText("");
            updatecommetcount();
        }).addOnFailureListener(e -> {
            progressDialog.dismiss();
            Toast.makeText(PostDetailsActivity.this, "Failed", Toast.LENGTH_LONG).show();
        });
    }

    boolean count = false;

    private void updatecommetcount() {
        count = true;
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (count) {
                    String comments = "" + dataSnapshot.child("pcomments").getValue();
                    int newcomment = Integer.parseInt(comments) + 1;
                    reference.child("pcomments").setValue("" + newcomment);
                    count = false;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadUserInfo() {

        Query myref = FirebaseDatabase.getInstance().getReference("Users");
        myref.orderByChild("uid").equalTo(myuid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    myname = dataSnapshot1.child("uname").getValue().toString();
                    mydp = dataSnapshot1.child("image").getValue().toString();
                    try {
                        Glide.with(PostDetailsActivity.this).load(mydp).into(imagep);
                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadPostInfo() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");
        Query query = databaseReference.orderByChild("ptime").equalTo(postId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String descriptions = (dataSnapshot1.child("description").getValue() != null)
                            ? dataSnapshot1.child("description").getValue().toString()
                            : "";

                    uimage = (dataSnapshot1.child("uimage").getValue() != null)
                            ? dataSnapshot1.child("uimage").getValue().toString()
                            : "";

                    hisdp = (dataSnapshot1.child("udp").getValue() != null)
                            ? dataSnapshot1.child("udp").getValue().toString()
                            : "";

                    String uemail = (dataSnapshot1.child("uemail").getValue() != null)
                            ? dataSnapshot1.child("uemail").getValue().toString()
                            : "";

                    hisname = (dataSnapshot1.child("uname").getValue() != null)
                            ? dataSnapshot1.child("uname").getValue().toString()
                            : "";

                    ptime = (dataSnapshot1.child("ptime").getValue() != null)
                            ? dataSnapshot1.child("ptime").getValue().toString()
                            : "0";

                    plike = (dataSnapshot1.child("plike").getValue() != null)
                            ? dataSnapshot1.child("plike").getValue().toString()
                            : "";

                    String commentcount = (dataSnapshot1.child("pcomments").getValue() != null)
                            ? dataSnapshot1.child("pcomments").getValue().toString()
                            : "";

                    Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
                    calendar.setTimeInMillis(Long.parseLong(ptime));
                    String timedate = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

                    name.setText(hisname);
                    description.setText((descriptions != null) ? descriptions : "");
                    like.setText(((plike != null) ? plike : "") + " Likes");
                    time.setText((timedate != null) ? timedate : "");
                    tcomment.setText(((commentcount != null) ? commentcount : ""));


                    if (uimage.equals("noImage")) {
                        image.setVisibility(View.GONE);
                    } else {
                        image.setVisibility(View.VISIBLE);
                        try {
                            Glide.with(PostDetailsActivity.this).load(uimage).into(image);
                        } catch (Exception e) {
                            // Handle Glide exception
                        }
                    }

                    try {
                        Glide.with(PostDetailsActivity.this).load(hisdp).into(picture);
                    } catch (Exception e) {
                        // Handle Glide exception
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
