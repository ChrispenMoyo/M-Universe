package com.example.m_universe;

import static android.app.PendingIntent.getActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationBarView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import com.google.firebase.database.Query;
public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    FirebaseUser firebaseUser;

    String myuid;
   // ActionBar actionBar;
    NavigationBarView navigationView;
    TextView toolbarTitle;
    AdapterPosts adapterPosts;
    private RecyclerView recyclerViewPosts;
    ImageView profileIcon,filterIcon,searchIcon, app_icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        // App Icon & Toolbar title
        toolbarTitle = findViewById(R.id.toolbar_title);
        app_icon = findViewById(R.id.app_icon);

        recyclerViewPosts = findViewById(R.id.recyclerViewPosts);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewPosts.setLayoutManager(layoutManager);

        // Handle clicks on the toolbar title
        toolbarTitle.setOnClickListener(view -> goToHomeFragment());

        // Handle clicks on the app icon
        app_icon.setOnClickListener(view -> goToHomeFragment());

        // Handle clicks on the search icon
        searchIcon = findViewById(R.id.search_icon);

        searchIcon.setOnClickListener(view -> {
            Intent searchIntent = new Intent(DashboardActivity.this, search.class);
            startActivity(searchIntent);
        });

        // Handle clicks on the filter icon
        filterIcon = findViewById(R.id.filter_icon);

        PopupMenu popupMenu2 = new PopupMenu(this, filterIcon); // "this" refers to the context
        // Inflate the menu resource you created
        popupMenu2.getMenuInflater().inflate(R.menu.sort_filter, popupMenu2.getMenu());

        // Set a click listener for Sort 1
        popupMenu2.getMenu().findItem(R.id.sort_1).setOnMenuItemClickListener(item -> {
            loadPosts(false); // Load recent posts with newest posts at the top
            return true;
        });

        // Set a click listener for Sort 2
        popupMenu2.getMenu().findItem(R.id.sort_2).setOnMenuItemClickListener(item -> {
            loadPosts(true); // Load recent posts with oldest posts at the top
            return true;
        });



        filterIcon.setOnClickListener(v -> popupMenu2.show());


        // Handle clicks on the profile icon
        profileIcon = findViewById(R.id.profile_icon);

        //new
        PopupMenu popupMenu = new PopupMenu(this, profileIcon); // "this" refers to the context
        // Inflate the menu resource you created
        popupMenu.getMenuInflater().inflate(R.menu.profile_dropdown, popupMenu.getMenu());

        // Set a click listener for Item 1
        popupMenu.getMenu().findItem(R.id.menu_item_1).setOnMenuItemClickListener(item -> {

            // Handle the click for Item 1 (Navigate to ProfileFragment)
            startActivity(new Intent(this, Profile.class));
            return true;
        });

        // Set a click listener for Item 2
        popupMenu.getMenu().findItem(R.id.menu_item_2).setOnMenuItemClickListener(item -> {
            // Handle the click for Item 2 (Navigate to EditProfilePage)
            startActivity(new Intent(this, EditProfilePage.class));
            return true;
        });

        // Set a click listener for Item 3
        popupMenu.getMenu().findItem(R.id.menu_item_3).setOnMenuItemClickListener(item -> {
            // Handle the click for Item 3 (Logout)
            // Sign out the user from Firebase Authentication
            firebaseAuth.signOut();

            // Create an intent to navigate to the LoginActivity
            Intent intent = new Intent(this, LoginActivity.class);

            // Add flags to clear the task stack and start a new task
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

            // Start the LoginActivity
            startActivity(intent);
            return true;
        });


        // Set a click listener for your button to show the dropdown menu
        profileIcon.setOnClickListener(v -> popupMenu.show());

        /*old
        profileIcon.setOnClickListener(view -> {
                Intent i = new Intent(DashboardActivity.this, EditProfilePage.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            // Add code to handle the profile icon click
            // For example, open the user's profile page
            // Intent profileIntent = new Intent(this, ProfileActivity.class);
            // startActivity(profileIntent);

        });  */








        // Initialize the action bar
      /* actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Home");
        } */

        // Initialize Firebase Authentication
        firebaseAuth = FirebaseAuth.getInstance();

        // Initialize the navigation bar
        navigationView = findViewById(R.id.navigation);



        // Set the selectedListener for the navigation bar
        navigationView.setOnItemSelectedListener(menuItem -> {

            // Handle navigation item selection here
            int itemId = menuItem.getItemId();

            if (itemId == R.id.nav_home) {
                // Handle Home selection
                //actionBar.setTitle("Home");
                HomeFragment fragment = new HomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content, fragment, "");
                fragmentTransaction.commit();
                menuItem.setChecked(true);
                return true;
            }
            else if (itemId == R.id.nav_communities) {
                //actionBar.setTitle("Communities");
                CommunitiesFragment fragment1 = new CommunitiesFragment();
                FragmentTransaction fragmentTransaction1 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction1.replace(R.id.content, fragment1,"");
                fragmentTransaction1.commit();
                menuItem.setChecked(true);
                return true;
            }
            else if (itemId == R.id.nav_add) {
                //actionBar.setTitle("New Post");
                AddFragment fragment2 = new AddFragment();
                FragmentTransaction fragmentTransaction2 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction2.replace(R.id.content, fragment2, "");
                fragmentTransaction2.commit();
                menuItem.setChecked(true);
                return true;
            }
            else if (itemId == R.id.nav_notifications) {
                //actionBar.setTitle("Notifications");
                NotificationsFragment fragment3 = new NotificationsFragment();
                FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction3.replace(R.id.content, fragment3, "");
                fragmentTransaction3.commit();
                menuItem.setChecked(true);
                return true;
            }
            else if (itemId == R.id.nav_chat) {
               //actionBar.setTitle("Chat");
                ChatFragment fragment4 = new ChatFragment();
                FragmentTransaction fragmentTransaction4 = getSupportFragmentManager().beginTransaction();
                fragmentTransaction4.replace(R.id.content, fragment4, "");
                fragmentTransaction4.commit();
                menuItem.setChecked(true);
                return true;
            }

            return false;
        });

        // When the application first opens, show the HomeFragment
       /* if (actionBar != null) {
            actionBar.setTitle("Home");
        } */

        // When we open the application first
        // time the fragment should be shown to the user
        // in this case it is home fragment
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();
    }

    private void goToHomeFragment() {
        loadPosts(false); // Load recent posts with newest posts at the top
        HomeFragment fragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, fragment, "");
        fragmentTransaction.commit();
    }

    private void loadPosts(boolean sortByAscending) {
        // Assuming you are using Firebase as your backend
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Posts");

        Query query;
        if (sortByAscending) {
            query = databaseReference.orderByChild("timestamp");
        } else {
            query = databaseReference.orderByChild("timestamp").limitToLast(20);
        }

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<ModelPost> posts = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ModelPost modelPost = snapshot.getValue(ModelPost.class);
                    if (modelPost != null) {
                        posts.add(modelPost);
                    }
                }

                if (adapterPosts == null) {
                    // Initialize the adapter only if it's not initialized yet
                    adapterPosts = new AdapterPosts(DashboardActivity.this, posts);
                    recyclerViewPosts.setAdapter(adapterPosts);
                }

                // Update the adapter's posts list
                adapterPosts.updatePosts(posts);

                // Call notifyDataSetChanged() only if adapterPosts is not null
                if (adapterPosts != null) {
                    adapterPosts.notifyDataSetChanged();
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors that occurred during the data retrieval
            }
        });
    }






}