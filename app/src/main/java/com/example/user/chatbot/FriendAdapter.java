package com.example.user.chatbot;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {
    List<Friend> mfriend;
    String stEmail;
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvEmail;
        public ImageView ivUser;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            ivUser = (ImageView) itemView.findViewById(R.id.ivUser);
        }
    }

    public FriendAdapter(List<Friend> mfriend) {
        this.mfriend = mfriend;
    }


    @Override
    public FriendAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_friend, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvEmail.setText(mfriend.get(position).getEmail());

    }

    @Override
    public int getItemCount() {
        return mfriend.size();
    }
}