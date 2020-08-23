package com.example.user.chatbot;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    EditText etEmail;
    EditText etPass;
    String stEmail;
    ProgressBar pbLogin;
    String stPass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //email , pass 구글에 넘기기
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPass);
        Button btnReg = (Button)findViewById(R.id.btnReg);
        pbLogin = (ProgressBar)findViewById(R.id.pbLogin);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(TAG,"signed_IN : " + user.getUid());
                }
                else{
                    Log.d(TAG,"signed_OUT : ");
                }
            }
        };
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = etEmail.getText().toString();
                stPass = etPass.getText().toString();

                if(stEmail.isEmpty() || stEmail.equals("")){
                     Toast.makeText(MainActivity.this,"Email을 입력하세요" , Toast.LENGTH_SHORT).show();
                }
                else if(stPass.isEmpty() || stPass.equals("")){
                    Toast.makeText(MainActivity.this,"Password를 입력하세요" , Toast.LENGTH_SHORT).show();
                }
                else{
                    registerUser(stEmail , stPass);
                }
               // Toast.makeText(MainActivity.this, stEmail+"  "+stPass , Toast.LENGTH_SHORT).show();

                //registerUser 함수 수행행

            }
        });

        //Mainactivity 에서 chatActivity로 넘어가기기
        //btnLogin을 찾아서 변수에 넣기기
        Button btnLogin = (Button)findViewById(R.id.btnLogin);

        //btnLogin 을 누르면 실행하는 함수
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stEmail = etEmail.getText().toString();
                stPass = etPass.getText().toString();

                if(stEmail.isEmpty() || stEmail.equals("")){
                    Toast.makeText(MainActivity.this,"Email을 입력하세요" , Toast.LENGTH_SHORT).show();
                }
                else if(stPass.isEmpty() || stPass.equals("")){
                    Toast.makeText(MainActivity.this,"Password를 입력하세요" , Toast.LENGTH_SHORT).show();
                }
                else{
                    userLogin(stEmail,stPass);
                }


                //버튼 누를 때 나오는 토스트 창
                //Toast.makeText(MainActivity.this, "Login" , Toast.LENGTH_SHORT).show();

                //클릭시 activity를 변경해라
                //Intent in = new Intent(MainActivity.this , ChatActivity.class);
                //activity변경
                //startActivity(in);

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    //firebase 에서 지원하는 가입 함수
    public void registerUser(String email , String password){
        pbLogin.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //성공시
                        if (!task.isSuccessful()) {
                            pbLogin.setVisibility(View.GONE);
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                        //실패시
                        else{
                            pbLogin.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Authentication success", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "createUserEmail : on complete : "+task.isSuccessful());
                        }
                        // ...
                    }
                });
    }

    //firebase에서 지원하는 로그인 함ㅅ구
    public void userLogin(String email , String password){
        pbLogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email , password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //실패시
                        if(!task.isSuccessful()){
                            pbLogin.setVisibility(View.GONE);
                            Log.v(TAG,"SigninwithEmail",task.getException());
                            Toast.makeText(MainActivity.this ,"Authentication failed",Toast.LENGTH_SHORT).show();
                        }
                        //성공시
                        else{
                            pbLogin.setVisibility(View.GONE);
                            Log.d(TAG , "onComplete"+task.isSuccessful());
                            Intent in = new Intent(MainActivity.this , MenuActivity.class);
                            //챗봇을 그냥 사용하려면 TabActivity -> ChatActivity로 넣으면된다.
                            startActivity(in);


                        }
                    }
                });
    }
}
