package com.example.messagingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.messagingapp.Adapters.MessageAdapter;
import com.example.messagingapp.Model.Contacts;
import com.example.messagingapp.Model.Messages;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {
String receiver_user_id,user_message,sender_user_id;
DatabaseReference userRef,messageRef,rootRef;
TextView uname,lastSeen;
CircleImageView circleImageView;
Toolbar toolbar;
RecyclerView recyclerView;
EditText message;
ImageButton send_message_button;
FirebaseUser firebaseUser;
List<Messages> messagesList=new ArrayList<>();
LinearLayoutManager linearLayoutManager;
MessageAdapter messageAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        toolbar=findViewById(R.id.toolbar);
        message=findViewById(R.id.editText);
        send_message_button=findViewById(R.id.imageButton);
        uname=findViewById(R.id.username);
        circleImageView=findViewById(R.id.user_profile_image);
        lastSeen=findViewById(R.id.last_seen);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");

        messageAdapter=new MessageAdapter(messagesList,this);
        linearLayoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView=findViewById(R.id.chats_recycler_view);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);





        receiver_user_id =getIntent().getExtras().getString("visit_user_id");


        userRef= FirebaseDatabase.getInstance().getReference().child("Users").child(receiver_user_id);
        messageRef=FirebaseDatabase.getInstance().getReference().child("Messages");
        rootRef=FirebaseDatabase.getInstance().getReference();
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        sender_user_id =firebaseUser.getUid();

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Contacts contacts=dataSnapshot.getValue(Contacts.class);
                uname.setText(contacts.getName());
                Glide.with(ChatActivity.this).load(contacts.getProfile_image()).placeholder(R.drawable.profile_image).centerCrop().into(circleImageView);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        send_message_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_message=message.getText().toString();

                if(TextUtils.isEmpty(user_message)) {
                    message.setError("enter the message");
                }
                else{
                    String messageSenderRef="Messages/"+sender_user_id+"/"+receiver_user_id;
                    String messageReceiverRef="Messages/"+receiver_user_id+"/"+sender_user_id;

                    DatabaseReference userMessageRef=FirebaseDatabase.getInstance().getReference().child("Messages").child(sender_user_id).child(receiver_user_id).push();
                    String messagePushID=userMessageRef.getKey();


                    Map messageTextBody=new HashMap();
                    messageTextBody.put("message",user_message);
                    messageTextBody.put("type","text");
                    messageTextBody.put("from",sender_user_id);

                    Map messageBodyDetails=new HashMap();
                    messageBodyDetails.put(messageSenderRef+"/"+messagePushID,messageTextBody);
                    messageBodyDetails.put(messageReceiverRef+"/"+messagePushID,messageTextBody);

                    rootRef.updateChildren(messageBodyDetails).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Toast.makeText(ChatActivity.this, "Message send!!!", Toast.LENGTH_SHORT).show();

                        }
                    });



                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        messageRef.child(sender_user_id).child(receiver_user_id).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Messages messages=dataSnapshot.getValue(Messages.class);
                messagesList.add(messages);
                messageAdapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
