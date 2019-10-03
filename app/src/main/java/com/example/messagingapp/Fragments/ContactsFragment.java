package com.example.messagingapp.Fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.messagingapp.Adapters.ContactsAdapter;
import com.example.messagingapp.Adapters.FindFriendsAdapter;
import com.example.messagingapp.Model.Contacts;
import com.example.messagingapp.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactsFragment extends Fragment {

RecyclerView recyclerView;
DatabaseReference contactRef;
FirebaseUser firebaseUser;
String currentUserID;
DatabaseReference userRef;
ContactsAdapter contactsAdapter;

public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_contacts,container,false);
        recyclerView=view.findViewById(R.id.contact_recycler_view);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        currentUserID=firebaseUser.getUid();
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");


        contactRef= FirebaseDatabase.getInstance().getReference().child("Contacts").child(currentUserID);

        FirebaseRecyclerOptions<Contacts> options=new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(contactRef,Contacts.class)
                .build();

        contactsAdapter=new ContactsAdapter(options,getContext(),userRef);
        recyclerView.setAdapter(contactsAdapter);




        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        contactsAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        contactsAdapter.stopListening();
    }
}
