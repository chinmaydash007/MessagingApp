package com.example.messagingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAcivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView username, user_status;
    Button sendRequest,cancelRequest;
    DatabaseReference databaseReference, chatsRequestRef;
    String receiver_userId, senderUserId, currentState;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_acivity);
        receiver_userId = getIntent().getExtras().getString("visit_user_id");
        circleImageView = findViewById(R.id.circleImageView);
        username = findViewById(R.id.textView3);
        user_status = findViewById(R.id.textView5);
        sendRequest = findViewById(R.id.send_request);
        cancelRequest=findViewById(R.id.receive_request);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        chatsRequestRef = FirebaseDatabase.getInstance().getReference().child("Chats_Request");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        currentState = "new";
        senderUserId = currentUser.getUid();


        RetriveUserData();
    }


    // Retreive user data like username,status,profile_image if available
    private void RetriveUserData() {
        databaseReference.child(receiver_userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    String uname=dataSnapshot.child("name").getValue().toString();
                    String status=dataSnapshot.child("status").getValue().toString();
                    username.setText(uname);
                    user_status.setText(status);

                    ManageChatRequest();


                }
                if(dataSnapshot.hasChild("profile_image")){
                    String profile_image_url=dataSnapshot.child("profile_image").getValue().toString();
                    Glide.with(ProfileAcivity.this).load(profile_image_url).placeholder(R.drawable.profile_image).centerCrop().into(circleImageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ManageChatRequest() {


        //Code for those whom you have send chat request already..
        chatsRequestRef.child(senderUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(receiver_userId)){
                    String request_type=dataSnapshot.child(receiver_userId).child("request_type").getValue().toString();
                    if(request_type.equals("send")){
                        currentState="request_send";
                        sendRequest.setText("Cancel Request");
                    }
                    else if(request_type.equals("received")){
                        currentState="request_received";
                        sendRequest.setText("Accept Chat Request");
                        cancelRequest.setVisibility(View.VISIBLE);
                        cancelRequest.setEnabled(true);
                        cancelRequest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CancelChatRequest();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
    });

        //Code for whom you can send chat request except yourself.
        if(!senderUserId.equals(receiver_userId)){
            sendRequest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendRequest.setEnabled(false);
                    if(currentState.equals("new")){
                        SendChatRequest();

                    }
                    if(currentState.equals("request_send")){
                        CancelChatRequest();
                    }
                }
            });
        }
        else{
            sendRequest.setVisibility(View.INVISIBLE);
        }
    }

    private void CancelChatRequest() {
        chatsRequestRef.child(senderUserId).child(receiver_userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                chatsRequestRef.child(receiver_userId).child(senderUserId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProfileAcivity.this, "Request cancelled.", Toast.LENGTH_SHORT).show();
                        sendRequest.setEnabled(true);
                        sendRequest.setText("Send Request");
                        currentState="new";
                        cancelRequest.setVisibility(View.INVISIBLE);
                        cancelRequest.setEnabled(false);

                    }
                });
            }
        });
    }

    //Send Chat request from sender to receiver with request code="sent"
    //Get Chat request from sender with request code="received"
    private void SendChatRequest() {
        chatsRequestRef.child(senderUserId).child(receiver_userId).child("request_type").setValue("send").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                chatsRequestRef.child(receiver_userId).child(senderUserId).child("request_type").setValue("received").addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                            sendRequest.setEnabled(true);
                            currentState="request_send";
                            sendRequest.setText("cancel Request");

                    }
                });
            }
        });
    }
}
