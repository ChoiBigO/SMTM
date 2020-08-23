package com.example.user.chatbot;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private String[] mDataset;
    List<Chat> mChat;
    String stEmail;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.mTextView);

        }
    }

    public MyAdapter(List<Chat> mChat , String email) {
        this.mChat = mChat;
        this.stEmail = email;
    }
    @Override
    public int getItemViewType(int position) {
//        mChat에서 가져오는 email과 같다면 if수행
        if(mChat.get(position).getEmail().equals(stEmail)) return 1;
        else return 2;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        View v;
        //text를 외쪽에 쓸껀가 오른쪽에 쓸껀가 결정
        if(viewType == 1){
            v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.right_text_view, parent, false);
        }
        else{
            v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.left_text_view, parent, false);
        }

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.mTextView.setText(mChat.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }
}