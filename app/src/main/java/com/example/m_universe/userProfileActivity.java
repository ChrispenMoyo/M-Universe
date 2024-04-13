// UserProfileActivity.java
package com.example.m_universe;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class userProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView avatartv;
    TextView nam, email, grnum;
    RecyclerView postrecycle;
    ProgressBar pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");

        // Initialising the text view and imageview
        avatartv = findViewById(R.id.avatartv);
        nam = findViewById(R.id.nametv);
        email = findViewById(R.id.emailtv);
        grnum = findViewById(R.id.gr_numtv);
        pd = findViewById(R.id.progressBar);

        // Show the ProgressBar while loading the data
        pd.setVisibility(View.VISIBLE);

        /// Get the username of the user who made the post
        String username = getIntent().getStringExtra("uname");

        if (username != null) {
            // Get the user details using the username
            Query query = databaseReference.orderByChild("uname").equalTo(username);

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Check if the dataSnapshot contains any data
                    if (dataSnapshot.exists()) {
                        // Retrieving Data from firebase
                        DataSnapshot dataSnapshot1 = dataSnapshot.getChildren().iterator().next();
                        String name = "" + dataSnapshot1.child("uname").getValue();
                        String emaill = "" + dataSnapshot1.child("email").getValue();
                        String gr_number = "" + dataSnapshot1.child("gr num").getValue();
                        String image = "" + dataSnapshot1.child("image").getValue();

                        // setting data to our text view
                        nam.setText(name);
                        email.setText(emaill);
                        grnum.setText(gr_number);

                        try {
                            Glide.with(userProfileActivity.this).load(image).into(avatartv);
                        } catch (Exception e) {
                            // Handle Glide exception if needed
                        }
                    } else {
                        // Handle the case when no data is found for the given username
                        // For example, show an error message or finish the activity
                        finish();
                    }

                    // Hide the ProgressBar when the data has been loaded
                    pd.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle onCancelled if needed
                }
            });
        } else {
            // Handle the case when username is null
            // For example, show an error message or finish the activity
            finish();
        }

    }
}
