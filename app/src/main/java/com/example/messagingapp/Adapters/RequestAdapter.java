package com.example.messagingapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.messagingapp.Model.Contacts;
import com.example.messagingapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapter extends FirebaseRecyclerAdapter<Contacts, RequestAdapter.RequestViewHolder> {
    private static final String TAG ="test" ;
    DatabaseReference userRef,contactsRef,chatsRef;
    Context context;
    String userids;
    String currentuserID;



    public RequestAdapter(@NonNull FirebaseRecyclerOptions<Contacts> options,Context context) {
        super(options);
        this.context=context;
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        contactsRef=FirebaseDatabase.getInstance().getReference().child("Contacts");
        chatsRef=FirebaseDatabase.getInstance().getReference().child("Chats_Request");

        Log.d("test", "RequestAdapter: ");
        currentuserID= FirebaseAuth.getInstance().getUid();

    }


    @Override
    protected void onBindViewHolder(@NonNull final RequestViewHolder holder, int position, @NonNull Contacts model) {

        userids = getRef(position).getKey();
        Log.d(TAG, userids);
        userRef.child(userids).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String username = dataSnapshot.child("name").getValue().toString();
                    String status = dataSnapshot.child("status").getValue().toString();
                    holder.username.setText(username);
                    holder.status.setText(status);

                if (dataSnapshot.hasChild("profile_image")) {
                    String profile_image = dataSnapshot.child("profile_image").getValue().toString();
                    Glide.with(context).load(profile_image).placeholder(R.drawable.profile_image).centerCrop().into(holder.circleImageView);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout_for_request, parent, false);
        RequestViewHolder requestViewHolder = new RequestViewHolder(view);
        Log.d("test", "onCreateViewHolder: ");
        return requestViewHolder;
    }


    class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView status;
        CircleImageView circleImageView;
        Button acceptButton, rejectButton;


        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.textView);
            status = itemView.findViewById(R.id.textView2);
            circleImageView = itemView.findViewById(R.id.imageView);
            acceptButton = itemView.findViewById(R.id.accept_friend_request);
            rejectButton = itemView.findViewById(R.id.decline_friend_request);

            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   contactsRef.child(userids).child(currentuserID).child("Contacts").setValue("saved").addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void aVoid) {
                           contactsRef.child(currentuserID).child(userids).child("Contacts").setValue("saved").addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   chatsRef.child(currentuserID).child(userids).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void aVoid) {
                                           chatsRef.child(userids).child(currentuserID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                               @Override
                                               public void onSuccess(Void aVoid) {
                                                   Toast.makeText(context,"You are now friends",Toast.LENGTH_SHORT).show();
                                               }
                                           });
                                       }
                                   });
                               }
                           });
                       }
                   });

                }
            });

            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    chatsRef.child(currentuserID).child(userids).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            chatsRef.child(userids).child(currentuserID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(context, "Request denied", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                    });
                }
            });


        }

    }

}
