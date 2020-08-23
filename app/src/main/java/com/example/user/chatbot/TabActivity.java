package com.example.user.chatbot;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class TabActivity extends AppCompatActivity {
    long lastPressted = 0;
    Fragment fragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   fragment = new HomeFragment();
                    switchFragment(fragment);
                    return true;
                case R.id.navigation_dashboard:
                    fragment = new FriendFragment();
                    switchFragment(fragment);
                    return true;
                case R.id.navigation_notifications:
                    fragment = new ProfileFragment();
                    switchFragment(fragment);
                    return true;
            }
            return false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        //fragment를 가져와서 보여준다
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        HomeFragment fragment = new HomeFragment();
        fragmentTransaction.add(R.id.content , fragment);
        fragmentTransaction.commit();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void switchFragment(Fragment newfragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content , newfragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {

        if(System.currentTimeMillis() - lastPressted< 1500){
            finish();
        }
        else{
            Toast.makeText(this,"한번더 누를시 종료",Toast.LENGTH_SHORT).show();
            lastPressted = System.currentTimeMillis();
       }
    }
}
