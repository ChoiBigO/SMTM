package com.example.user.chatbot;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class FriendFragment extends Fragment {

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    String email;
    FirebaseDatabase database  = FirebaseDatabase.getInstance(); //firebasedatabase 를 연결
    private List<Friend> mFriend;
    FriendAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_friend, container, false);

        //RecycleView 기능
        recyclerView = (RecyclerView)v.findViewById(R.id.rFriend);

        // use this setting to improve performance if you know that changes
        //RecyclerView의 크기를 정해준다
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mFriend = new ArrayList<>();
        mAdapter = new FriendAdapter(mFriend);
        recyclerView.setAdapter(mAdapter);

        FirebaseDatabase.getInstance();

        DatabaseReference myRef1 = database.getReference("chats");

        myRef1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Friend friend = dataSnapshot.getValue(Friend.class);

                mFriend.add(friend);
                recyclerView.scrollToPosition(mFriend.size()-1);
                mAdapter.notifyItemInserted(mFriend.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

}
