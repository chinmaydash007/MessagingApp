package com.example.messagingapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messagingapp.Model.Contacts;
import com.example.messagingapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends FirebaseRecyclerAdapter<Contacts, ContactsAdapter.ContactsViewHolder> {
    Context context;
    DatabaseReference userRef;

    public ContactsAdapter(@NonNull FirebaseRecyclerOptions<Contacts> options, Context context, DatabaseReference userRef) {
        super(options);
        this.context=context;
        this.userRef=userRef;
    }

    @Override
    protected void onBindViewHolder(@NonNull final ContactsViewHolder holder, int position, @NonNull final Contacts model) {
        String userIds=getRef(position).getKey();



        userRef.child(userIds).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("profile_image")){
                    String uname=dataSnapshot.child("name").getValue().toString();
                    String status=dataSnapshot.child("status").getValue().toString();
                    String profile_image=dataSnapshot.child("profile_image").getValue().toString();
                    holder.username.setText(uname);
                    holder.status.setText(status);
                    Glide.with(context).load(profile_image).placeholder(R.drawable.profile_image).centerCrop().into(holder.circleImageView);

                }
                else{
                    String uname=dataSnapshot.child("name").getValue().toString();
                    String status=dataSnapshot.child("status").getValue().toString();
                    holder.username.setText(uname);
                    holder.status.setText(status);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    @NonNull
    @Override
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
        ContactsViewHolder contactsViewHolder=new ContactsViewHolder(view);
        return contactsViewHolder;
    }

    class ContactsViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView status;
        CircleImageView circleImageView;
        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.textView);
            status=itemView.findViewById(R.id.textView2);
            circleImageView=itemView.findViewById(R.id.imageView);

        }
    }
}
