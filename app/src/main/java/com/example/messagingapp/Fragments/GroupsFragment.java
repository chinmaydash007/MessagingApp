package com.example.messagingapp.Fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.messagingapp.GroupChatActivity;
import com.example.messagingapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class GroupsFragment extends Fragment {
    ListView listView;
    ArrayList<String> arrayList;
    ArrayAdapter<String> arrayAdapter;
    DatabaseReference databaseReference;



    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_groups,container,false);
        listView=view.findViewById(R.id.group_list_view);
        arrayList=new ArrayList<>();
        arrayAdapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("Groups");

        RetriveAndDisplayGroups();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String currentGroup=adapterView.getItemAtPosition(position).toString();
                Intent intent=new Intent(getContext(), GroupChatActivity.class);
                intent.putExtra("groupName",currentGroup);
                startActivity(intent);

            }
        });

        return view;
    }

    private void RetriveAndDisplayGroups() {
        databaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Set<String> set=new HashSet<>();
                    Iterator iterator=dataSnapshot.getChildren().iterator();
                    while (iterator.hasNext()){
                        set.add(((DataSnapshot)iterator.next()).getKey());

                    }
                    arrayList.clear();
                    arrayList.addAll(set);
                    arrayAdapter.notifyDataSetChanged();
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
