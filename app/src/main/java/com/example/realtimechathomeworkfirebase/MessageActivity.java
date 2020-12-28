package com.example.realtimechathomeworkfirebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.realtimechathomeworkfirebase.Adapters.MessageAdapter;
import com.example.realtimechathomeworkfirebase.Model.Chat;
import com.example.realtimechathomeworkfirebase.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    /**
     * .
     */
    private CircleImageView profileImage;
    /**
     * .
     */
    private TextView username;
    /**
     * .
     */
    private FirebaseUser fireBaseUser;
    /**
     * .
     */
    private DatabaseReference reference;
    /**
     * .
     */
    private RecyclerView recyclerView;
    /**
     * .
     */
    private ImageButton btnSend;
    /**
     * .
     */
    private EditText textSend;
    /**
     * .
     */
    private Intent intent;
    /**
     * .
     */
    private ValueEventListener seenListener;
    /**
     * .
     */
    private MessageAdapter messageAdapter;
    /**
     * .
     */
    private List<Chat> mchats;
    /**
     * .
     */
    private String userid;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        //settings for Action Bar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                startActivity(
                        new Intent(MessageActivity.this,
                                MainActivity.class)
                                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        //ui reference
        profileImage = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        textSend = findViewById(R.id.text_send);
        btnSend = findViewById(R.id.btn_send);


        //recieve data from MainActivity (Users Fragment)
        // (the action excute in userAdapter --OnBind method--)
        intent = getIntent();
        userid = intent.getStringExtra("userid");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String message = textSend.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    sendMessage(fireBaseUser.getUid(), userid, message);
                } else {
                    Toast.makeText(MessageActivity.this,
                            "You can't send an empty message?!",
                            Toast.LENGTH_SHORT)
                            .show();
                }
                textSend.setText("");
            }
        });
        //make recycler view stand by and give it adapter
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(
                getApplicationContext());
        linearLayout.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayout);

        //get user inf from firebase
        fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageUrl() == "default") {
                    profileImage.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext())
                            .load(user.getImageUrl())
                            .into(profileImage);
                }
                readMessages(fireBaseUser.getUid(), userid, user.getImageUrl());
            }

            @Override
            public void onCancelled(
                    @NonNull final DatabaseError databaseError
            ) {

            }
        });
        seenMessage(userid);

    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
        reference.removeEventListener(seenListener);

    }

    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("chats");
        seenListener = reference.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(
                            @NonNull final DataSnapshot dataSnapshot
                    ) {
                        for (DataSnapshot snapshot
                                : dataSnapshot.getChildren()
                        ) {
                            Chat chat = snapshot.getValue(Chat.class);
                            if (chat != null) {
                                if (chat
                                        .getReceiver()
                                        .equals(fireBaseUser
                                                .getUid())
                                        && chat
                                        .getSender()
                                        .equals(userid)) {
                                    HashMap<String, Object> hashMap =
                                            new HashMap<>();
                                    hashMap.put("isseen", true);
                                    snapshot.getRef().updateChildren(hashMap);


                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(
                            @NonNull final DatabaseError databaseError
                    ) {

                    }
                });

    }

    /**
     * .
     */
    /*
     *@param status;
     */
    public void status(final String status) {
        reference = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(fireBaseUser.getUid());
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        reference.updateChildren(hashMap);
    }

    /**
     * .
     */
    /*
     *@param sender;
     *@param receiver
     *@param message
     */
    public void sendMessage(
            final String sender,
            final String receiver,
            final String message
    ) {
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        if (Validation.checkMessage(message)) {
            hashMap.put("message", message);
        } else {
            Toast.makeText(this,
                    "The message Field is Empty!!",
                    Toast.LENGTH_SHORT)
                    .show();
        }

        hashMap.put("isseen", false);
        reference.child("chats").push().setValue(hashMap);
        final DatabaseReference chatref = FirebaseDatabase.getInstance()
                .getReference("ChatList")
                .child(fireBaseUser.getUid()).child(userid);
        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatref.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(
                    @NonNull final DatabaseError databaseError
            ) {

            }
        });
    }

    private void readMessages(
            final String myid,
            final String userid,
            final String imageUrl
    ) {

        mchats = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                mchats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (
                            chat.getReceiver().equals(myid)
                                    && chat.getSender().equals(userid
                            )
                                    || chat.getReceiver().equals(userid)
                                    && chat.getSender().equals(myid)
                    ) {
                        mchats.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this,
                            mchats,
                            imageUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(
                    @NonNull final DatabaseError databaseError
            ) {

            }
        });
    }
}
