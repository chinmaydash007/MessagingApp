package com.example.messagingapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.messagingapp.FindFreindsActivity;
import com.example.messagingapp.Model.Contacts;
import com.example.messagingapp.ProfileAcivity;
import com.example.messagingapp.R;
import com.example.messagingapp.SettingsActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.Query;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindFriendsAdapter extends FirebaseRecyclerAdapter<Contacts, FindFriendsAdapter.ContactHolder> {

    Context context;

    public FindFriendsAdapter(@NonNull FirebaseRecyclerOptions<Contacts> options,Context context) {
        super(options);
        this.context=context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ContactHolder holder, final int position, @NonNull Contacts model) {
        holder.username.setText(model.getName());
        holder.status.setText(model.getStatus());
        Glide.with(context).load(model.getProfile_image()).placeholder(R.drawable.profile_image).centerCrop().into(holder.circleImageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String visit_user_id=getRef(position).getKey();
                Intent profile_intent=new Intent(context, ProfileAcivity.class);
                profile_intent.putExtra("visit_user_id",visit_user_id);
                context.startActivity(profile_intent);

            }
        });

    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.user_display_layout,parent,false);
        ContactHolder contactHolder=new ContactHolder(view);
        return contactHolder;
    }

    class ContactHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView status;
        CircleImageView circleImageView;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.textView);
            status=itemView.findViewById(R.id.textView2);
            circleImageView=itemView.findViewById(R.id.imageView);

        }
    }
}
