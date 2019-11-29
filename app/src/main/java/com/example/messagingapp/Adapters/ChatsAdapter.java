package com.example.messagingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messagingapp.ChatActivity;
import com.example.messagingapp.Model.Contacts;
import com.example.messagingapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsAdapter extends FirebaseRecyclerAdapter<Contacts, ChatsAdapter.ChatsViewHolder> {
    Context context;
    DatabaseReference userRef;



    public ChatsAdapter(@NonNull FirebaseRecyclerOptions<Contacts> options,Context context,DatabaseReference userRef) {
        super(options);
        this.context=context;
        this.userRef=userRef;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ChatsViewHolder holder, int position, @NonNull Contacts model) {
        final String userIds=getRef(position).getKey();

        userRef.child(userIds).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("profile_image")){
                    String uname=dataSnapshot.child("name").getValue().toString();
                    String status=dataSnapshot.child("status").getValue().toString();
                    String profile_image=dataSnapshot.child("profile_image").getValue().toString();
                    holder.name.setText(uname);
                    holder.last_seen.setText(status);
                    Glide.with(context).load(profile_image).placeholder(R.drawable.profile_image).centerCrop().into(holder.profile_image);

                }
                else{
                    String uname=dataSnapshot.child("name").getValue().toString();
                    String status=dataSnapshot.child("status").getValue().toString();
                    holder.name.setText(uname);
                    holder.last_seen.setText(status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ChatActivity.class);
                intent.putExtra("visit_user_id",userIds);
                Toast.makeText(context, userIds, Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_diaplay_layout_for_contacts,parent,false);
        ChatsViewHolder chatsViewHolder=new ChatsViewHolder(view);
        return chatsViewHolder;

    }

    class ChatsViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView last_seen;
        CircleImageView profile_image;

        public ChatsViewHolder(@NonNull final View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView);
            last_seen=itemView.findViewById(R.id.textView2);
            profile_image=itemView.findViewById(R.id.imageView);



        }

    }
}
