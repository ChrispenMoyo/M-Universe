package com.example.m_universe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationsFragment() {
        // Required empty public constructor
    }

//    private void setupRecyclerView(RecyclerView recyclerView) {
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//
//        AdapterNotifications adapter = new AdapterNotifications(new ArrayList<>(), new AdapterNotifications.OnItemClickListener() {
//            @Override
//            public void onItemClick(Notification notification) {
//                // Handle click event here
//            }
//        });
//
//        // Add a ValueEventListener to fetch notifications only for the current user from the database
//        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
//
//        // Get the UID of the current user
//        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // Query notifications only for the current user
//        notificationsRef.orderByChild("userId").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<Notification> notifications = new ArrayList<>();
//
//                // Iterate through all child nodes (notifications) and add them to the list
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Notification notification = snapshot.getValue(Notification.class);
//                    notifications.add(notification);
//                }
//
//                // Check if there are any notifications
//                if (notifications.isEmpty()) {
//                    // Display "No Notifications" message
//                    showNoNotificationsMessage();
//                } else {
//                    // Update the adapter with the new list of notifications
//                    adapter.setNotifications(notifications);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle errors if needed
//            }
//        });
//
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void showNoNotificationsMessage() {
//        // Inflate a layout containing the "No Notifications" message
//        LayoutInflater inflater = LayoutInflater.from(getContext());
//        View noNotificationsView = inflater.inflate(R.layout.notification_no_notifications, null);
//
//        // Get the RecyclerView container
//        RecyclerView recyclerView = getView().findViewById(R.id.notificationsRecyclerView);
//
//        // Get the parent layout of the RecyclerView
//        RelativeLayout parentLayout = (RelativeLayout) recyclerView.getParent();
//
//        // Remove the RecyclerView and add the "No Notifications" message
//        parentLayout.removeView(recyclerView);
//        parentLayout.addView(noNotificationsView);
//    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        // Set up RecyclerView
//        RecyclerView recyclerView = view.findViewById(R.id.notificationsRecyclerView);
//        setupRecyclerView(recyclerView);
//
//        // Set up RecyclerView
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(layoutManager);
//
////        AdapterNotifications adapter = new AdapterNotifications(new ArrayList<>(),  new AdapterNotifications.OnItemClickListener());
////        recyclerView.setAdapter(adapter);
//
//        // Add a ValueEventListener to fetch notifications only for the current user from the database
//        DatabaseReference notificationsRef = FirebaseDatabase.getInstance().getReference().child("Notifications");
//
//        // Get the UID of the current user
//        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//
//        // Query notifications only for the current user
//        notificationsRef.orderByChild("userId").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<Notification> notifications = new ArrayList<>();
//
//                // Iterate through all child nodes (notifications) and add them to the list
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    Notification notification = snapshot.getValue(Notification.class);
//                    notifications.add(notification);
//                }
//
//                // Update the adapter with the new list of notifications
//               // adapter.setNotifications(notifications);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle errors if needed
//            }
//        });

        return view;
    }
}