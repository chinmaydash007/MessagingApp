package com.example.messagingapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.messagingapp.Adapters.FindFriendsAdapter;
import com.example.messagingapp.Model.Contacts;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FindFreindsActivity extends AppCompatActivity {
RecyclerView recyclerView;
Toolbar toolbar;

DatabaseReference userRef;
FindFriendsAdapter findFriendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_freinds);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Find Friends");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");



        recyclerView=findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        FirebaseRecyclerOptions<Contacts> firebaseRecyclerOptions=new FirebaseRecyclerOptions.Builder<Contacts>()
                .setQuery(userRef,Contacts.class)
                .build();



        findFriendsAdapter=new FindFriendsAdapter(firebaseRecyclerOptions,this);
        recyclerView.setAdapter(findFriendsAdapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        findFriendsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        findFriendsAdapter.stopListening();
    }
}
