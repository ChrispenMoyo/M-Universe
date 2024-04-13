package com.example.m_universe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

    FirebaseAuth firebaseAuth;
    RecyclerView recyclerView;
    List<ModelChatList> chatListList;
    List<User> usersList;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    AdapterChatList adapterChatList;
    List<ModelChat> chatList;
    TextView n1, n2, n3, n4, n5, n6 , n7, n8, n9, n10;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        firebaseAuth = FirebaseAuth.getInstance();

        n1 = view.findViewById(R.id.nameTextView);
        n2 = view.findViewById(R.id.nameTextView1);
        n3 = view.findViewById(R.id.nameTextView2);
        n4 = view.findViewById(R.id.nameTextView3);
        n5 = view.findViewById(R.id.nameTextView4);
        n6 = view.findViewById(R.id.nameTextView5);
        n7 = view.findViewById(R.id.nameTextView6);
        n8 = view.findViewById(R.id.nameTextView7);
        n9 = view.findViewById(R.id.nameTextView8);
        n10 = view.findViewById(R.id.nameTextView9);

        n1.setOnClickListener(v -> openChat());
        n2.setOnClickListener(v -> openChat());
        n3.setOnClickListener(v -> openChat());
        n4.setOnClickListener(v -> openChat());
        n5.setOnClickListener(v -> openChat());
        n6.setOnClickListener(v -> openChat());
        n7.setOnClickListener(v -> openChat());
        n8.setOnClickListener(v -> openChat());
        n9.setOnClickListener(v -> openChat());
        n10.setOnClickListener(v -> openChat());


        // getting current user
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        recyclerView = view.findViewById(R.id.chatlistrecycle);
//        chatListList = new ArrayList<>();
//        chatList = new ArrayList<>();
//        adapterChatList = new AdapterChatList(getActivity(), usersList);
//        Log.d("ChatFragment", "onCreateView: adapterChatList is set");
//        recyclerView.setAdapter(adapterChatList);
//        reference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
//        if (chatListList.isEmpty()) {
//            createNewChat();
//            return view;
//        }
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                chatListList.clear();
//
//
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    ModelChatList modelChatList = ds.getValue(ModelChatList.class);
//                    if (!modelChatList.getId().equals(firebaseUser.getUid())) {
//                        chatListList.add(modelChatList);
//                    }
//
//                }
//                loadChats();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        return view;

    }

    private  void openChat() {
        Intent intent = new Intent(requireActivity(), chat.class);
        startActivity(intent);
    }

    // loading the user chat layout using chat node
//    private void loadChats() {
//        usersList = new ArrayList<>();
//        reference = FirebaseDatabase.getInstance().getReference("Users");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                usersList.clear();
//                if (chatListList.isEmpty()) {
//                    createNewChat(); // Create new chat if chat list is empty
//                    return;
//                }
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    User user = dataSnapshot1.getValue(User.class);
//                    for (ModelChatList chatList : chatListList) {
//                        if (user.getUid() != null && user.getUid().equals(chatList.getId())) {
//                            Log.d("ChatFragment", "User: " + user.getUname());
//                            usersList.add(user);
//                            break;
//                        }
//                    }
//                    // Initialize the adapter with the updated usersList
//                    if (!usersList.isEmpty()) {
//                        Log.d("ChatFragment", "usersList size: " + usersList.size());
//                        adapterChatList = new AdapterChatList(getActivity(), usersList);
//                        recyclerView.setAdapter(adapterChatList);
//                    } else {
//                        Log.d("ChatFragment", "usersList is empty");
//                    }
//
//
//
//                    // getting last message of the user
//                    for (int i = 0; i < usersList.size(); i++) {
//                        lastMessage(usersList.get(i).getUid());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("ChatFragment", "Error fetching data: " + databaseError.getMessage());
//            }
//        });
//    }
//
//    private void lastMessage(final String uid) {
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Chats");
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String lastmess = "default";
//                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
//                    ModelChat chat = dataSnapshot1.getValue(ModelChat.class);
//                    if (chat == null) {
//                        continue;
//                    }
//                    String sender = chat.getSender();
//                    String receiver = chat.getReceiver();
//                    if (sender == null || receiver == null) {
//                        continue;
//                    }
//                    // checking for the type of message if
//                    // message type is image then set
//                    // last message as sent a photo
//                    if (chat.getReceiver().equals(firebaseUser.getUid()) &&
//                            chat.getSender().equals(uid) ||
//                            chat.getReceiver().equals(uid) &&
//                                    chat.getSender().equals(firebaseUser.getUid())) {
//                        if (chat.getType().equals("images")) {
//                            lastmess = "Sent a Photo";
//                        } else {
//                            lastmess = chat.getMessage();
//                        }
//                    }
//                }
//                adapterChatList.setlastMessageMap(uid, lastmess);
//                adapterChatList.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
//
//    private void createNewChat() {
//        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
//        Query query = usersRef.orderByChild("online").equalTo(true);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (!dataSnapshot.exists()) {
//                    return;
//                }
//
//                // Get a random user
//                int randomIndex = new Random().nextInt((int) dataSnapshot.getChildrenCount());
//                Iterator<DataSnapshot> iterator = dataSnapshot.getChildren().iterator();
//                for (int i = 0; i < randomIndex; i++) {
//                    if (iterator.hasNext()) {
//                        iterator.next();
//                    }
//                }
//                DataSnapshot randomUserSnapshot = iterator.next();
//
//                // Create a new chat with the current user and the random user
//                String randomUid = randomUserSnapshot.getKey();
//                ModelChatList chatList = new ModelChatList(firebaseUser.getUid(), randomUid);
//                reference = FirebaseDatabase.getInstance().getReference("ChatList");
//                reference.child(firebaseUser.getUid()).child(randomUid).setValue(chatList);
//                reference.child(randomUid).child(firebaseUser.getUid()).setValue(chatList);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

}
