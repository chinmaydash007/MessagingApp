package com.example.messagingapp.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messagingapp.Adapters.RequestAdapter;
import com.example.messagingapp.Model.Contacts;
import com.example.messagingapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RequestFragment extends Fragment {
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DatabaseReference chatsRef;
    RequestAdapter requestAdapter;
    FirebaseUser firebaseUser;
    String currentUserID;


    public RequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUserID = firebaseUser.getUid();


        recyclerView = view.findViewById(R.id.request_recycler_view);
        linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        chatsRef = FirebaseDatabase.getInstance().getReference().child("Chats_Request").child(currentUserID);

        FirebaseRecyclerOptions<Contacts> options = new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(chatsRef, Contacts.class)
                .build();

        requestAdapter = new RequestAdapter(options, getContext());
        recyclerView.setAdapter(requestAdapter);
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        requestAdapter.stopListening();


    }

    @Override
    public void onStart() {
        super.onStart();
        requestAdapter.startListening();
    }
}
