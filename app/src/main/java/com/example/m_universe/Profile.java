package com.example.m_universe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.m_universe.EditProfilePage;
import com.example.m_universe.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import android.Manifest;


import java.util.List;
import java.util.Objects;

public class Profile extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ImageView avatartv;
    TextView nam, email;
    RecyclerView postrecycle;
    FloatingActionButton fab;
    ProgressBar pd;

    private static final int RC_CAMERA_AND_GALLERY = 100;
    private static final String[] CAMERA_AND_GALLERY_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();

        // getting current user data
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        // Initialising the text view and imageview
        avatartv = findViewById(R.id.avatartv);
        nam = findViewById(R.id.nametv);
        email = findViewById(R.id.emailtv);
        fab = findViewById(R.id.fab);
        pd = findViewById(R.id.progressBar);

        // On click we will open EditProfileActivity
        fab.setOnClickListener(v -> {
            if (nam != null && nam.getText() != null) {
                String currentName = nam.getText().toString();
                String currentImage = Objects.requireNonNull(avatartv.getTag()).toString();
                Intent intent = new Intent(Profile.this, EditProfilePage.class);
                intent.putExtra("current_name", currentName);
                intent.putExtra("current_image", currentImage);
                startActivity(intent);
            } else {
                // Handle the case when nam or nam.getText() is null
                Log.e("Profile", "nam TextView or nam.getText() is null");
            }

// Add this log statement to check if the nam TextView is initialized
            if (nam != null) {
                Log.i("Profile", "nam TextView is initialized");
            } else {
                Log.i("Profile", "nam TextView is NOT initialized");
            }
        });
        // Show the ProgressBar while loading the data
        pd.setVisibility(View.VISIBLE);
        Query query = databaseReference.orderByChild("email").equalTo(firebaseUser.getEmail());

        // Request the necessary permissions at runtime
        if (EasyPermissions.hasPermissions(this, CAMERA_AND_GALLERY_PERMISSIONS)) {
            // Already have permission, do nothing
        } else {
            EasyPermissions.requestPermissions(this, "This app needs access to the camera and gallery to function properly.",
                    RC_CAMERA_AND_GALLERY, CAMERA_AND_GALLERY_PERMISSIONS);
        }

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    // Retrieving Data from firebase
                    String name = "" + dataSnapshot1.child("uname").getValue();
                    String emaill = "" + dataSnapshot1.child("gr num").getValue();
                    String image = "" + dataSnapshot1.child("image").getValue();
                    // setting data to our text view
                    nam.setText(name);
                    email.setText(emaill);
                    try {
                        Glide.with(Profile.this).load(image).into(avatartv);
                    } catch (Exception e) {

                    }
                }
                // Hide the ProgressBar when the data has been loaded
                pd.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        // Some permissions have been granted, do nothing
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // Some permissions have been denied, show a message to the user
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
