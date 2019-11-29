package com.example.messagingapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messagingapp.Model.Messages;
import com.example.messagingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
List<Messages> messagesList;
DatabaseReference usersRef;
FirebaseAuth firebaseAuth;
Context context;


    public MessageAdapter(List<Messages> messagesList,Context context) {
        this.messagesList = messagesList;
        this.context=context;

    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_message_layout,parent,false);
        MessageViewHolder messageViewHolder=new MessageViewHolder(view);
        firebaseAuth=FirebaseAuth.getInstance();


        return messageViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder holder, int position) {
        String messageSenderId=firebaseAuth.getCurrentUser().getUid();
        final Messages messages=messagesList.get(position);

        String fromUserId=messages.getFrom();
        String fromMessageType=messages.getType();
        usersRef=FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.hasChild("profile_image")){
                        String receiverImage=dataSnapshot.child("profile_image").getValue().toString();
                        Glide.with(context).load(receiverImage).placeholder(R.drawable.profile_image).centerCrop().into(holder.receiverprofileImage);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if(fromMessageType.equals("text")) {
            holder.receiverMessageText.setVisibility(View.INVISIBLE);
            holder.receiverprofileImage.setVisibility(View.INVISIBLE);
            if(fromUserId.equals(messageSenderId)){
                holder.senderMessageText.setText(messages.getMessage());

            }
            else{
                holder.senderMessageText.setVisibility(View.INVISIBLE);
                holder.receiverprofileImage.setVisibility(View.VISIBLE);
                holder.receiverMessageText.setVisibility(View.VISIBLE);

                holder.receiverMessageText.setText(messages.getMessage());

            }
        }



    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder{
        TextView senderMessageText,receiverMessageText;
        CircleImageView receiverprofileImage;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessageText=itemView.findViewById(R.id.sender_message_text);
            receiverMessageText=itemView.findViewById(R.id.receiver_message_text);
            receiverprofileImage=itemView.findViewById(R.id.user_profile_image);

        }
    }

}
