package com.example.user.chatbot;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mData;

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);

        mData = new ArrayList<>();
        mData.add(new ColorFragment());
        mData.add(new Color2Fragment());
        mData.add(new Color3Fragment());
    }

    @Override
    public Fragment getItem(int i) {
        return mData.get(i);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {

            case 0:
                return "채팅";
            case 1:
                return "회사추천";

            case 2:
                return "ELS추천";
        }
        return null;


    }
}
