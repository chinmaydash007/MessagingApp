package com.example.messagingapp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.messagingapp.Adapters.ChatsAdapter;
import com.example.messagingapp.Model.Contacts;
import com.example.messagingapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ChatFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference userRef,contactRef;
    String currentUserID;
    ChatsAdapter chatsAdapter;
    FirebaseUser firebaseUser;







    public ChatFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        currentUserID=firebaseUser.getUid();
        contactRef= FirebaseDatabase.getInstance().getReference().child("Contacts");
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");


        FirebaseRecyclerOptions<Contacts> options=new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(contactRef.child(currentUserID),Contacts.class)
                .build();
        chatsAdapter=new ChatsAdapter(options,getContext(),userRef);

        recyclerView.setAdapter(chatsAdapter);








        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        chatsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        chatsAdapter.stopListening();
    }
}

