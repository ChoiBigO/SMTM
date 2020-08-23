package com.example.user.chatbot;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;


public class ChatActivity extends AppCompatActivity {



    private static String IP_ADDRESS = "iter7.jbnu.ac.kr/";
    private static String TAG = "phptest";
    //mysql 접속
    private String mJsonString;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    //        comment릉 위한 선언
    private List<Chat> mChat;
    private RecyclerView mRecyclerView;
    public String grap_url;
    EditText etText;
    Button btnSend;
    Button btnGrap;
    String email;
    String uid;
    FirebaseDatabase database  = FirebaseDatabase.getInstance(); //firebasedatabase 를 연결

    //bot_data firebase에 넣기
    public void botInsert(String bot_name ,  String bot_text , String select){
        Calendar c = Calendar.getInstance();
        //시간을 위한 값을 가져온다
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df2.format(c.getTime());
        //c의 시간을 가져온다
        DatabaseReference myRef2 = database.getReference(uid).child(formattedDate+select);
        //누르는 시간마다 chat에 child로 text가 생기게한다다
        //hashtable을 만든다
        Hashtable<String, String> bot = new Hashtable<String, String>();
        bot.put("email", bot_name); //나의 email을 "email"로 저장한다
        bot.put("text", bot_text);//내가 보낸 text를 text에 저장한다
        myRef2.setValue(bot); //hashtable을 실제로 넘겨서 저장한다
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etText = (EditText)findViewById(R.id.etText);
        btnSend = (Button)findViewById(R.id.btnSend);
        btnGrap = (Button)findViewById(R.id.Grap);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        //user Email가져오기
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        if (user != null) {
            email = user.getEmail();

        }


        //send 버튼을 누를시 text가 firebase로 넘어간다
        btnSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                String stText = etText.getText().toString();
                if(stText.equals("")||stText.isEmpty()){
                    Toast.makeText(ChatActivity.this, "내용을 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
                else {
                //Toast.makeText(ChatActivity.this, email + " " + stText, Toast.LENGTH_SHORT).show();
                //firebasedatabase 를 연결
//                    database = FirebaseDatabase.getInstance();
                //firebasedatabase 의 reference는 chat으로 하겟다

                Calendar c = Calendar.getInstance();
                //시간을 위한 값을 가져온다
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                //c의 시간을 가져온다

                DatabaseReference myRef = database.getReference(uid).child(formattedDate+"_User");
                //누르는 시간마다 chat에 child로 text가 생기게한다다
                //hashtable을 만든다
                Hashtable<String, String> chat = new Hashtable<String, String>();
                chat.put("email", email); //나의 email을 "email"로 저장한다
                chat.put("text", stText);//내가 보낸 text를 text에 저장한다
                myRef.setValue(chat); //hashtable을 실제로 넘겨서 저장한다


                InsertData task = new InsertData();
                task.execute("http://" + IP_ADDRESS + "/insert.php", email,stText);

                GetData get_task = new GetData();
                get_task.execute( "http://" + IP_ADDRESS + "/getjson.php", "");

                //text값 초기화
                etText.setText("");

            }
        }
        });
        Button btnfinish = (Button)findViewById(R.id.btnFinish);

        btnfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //MainActivity에서 넘어간 activity를 끝낸다
                finish();
            }
        });

        btnGrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(grap_url));
                startActivity(intent);

            }
        });


        //RecycleView 기능
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        //RecyclerView의 크기를 정해준다
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mChat = new ArrayList<>();
        mAdapter = new MyAdapter(mChat , email);
        recyclerView.setAdapter(mAdapter);

        //chats에 있는 것을 참조하겟다
        DatabaseReference myRef1 = database.getReference(uid);

        myRef1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Chat chat = dataSnapshot.getValue(Chat.class);

                mChat.add(chat);
                recyclerView.scrollToPosition(mChat.size()-1);
                mAdapter.notifyItemInserted(mChat.size()-1);
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
    }

    class InsertData extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ChatActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String name = (String)params[1];
            String chat_text = (String)params[2];

            String serverURL = (String)params[0];
            String postParameters = "name=" + name + "&chat_text=" + chat_text;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }


    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(ChatActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){

               // mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = params[1];


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(10000);
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }


    private void showResult(){

        String TAG_JSON="webnautes";
        String TAG_NAME = "name";
        String TAG_CHAT ="chat_text";
        String TAG_COM = "company";
        String TAG_MOD = "mode";
        String TAG_URL = "url";
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);



            JSONObject item = jsonArray.getJSONObject(0);
            String name = item.getString(TAG_NAME);
            String chat_text = item.getString(TAG_CHAT);
            String mode = item.getString(TAG_MOD);
            String url = item.getString(TAG_URL);
            String company = item.getString(TAG_COM);
            if(mode.equals("Future")){
                botInsert(name, chat_text, "_content");
                btnGrap.setText(company+"그래프 보기");
                btnGrap.setVisibility(View.VISIBLE);
                grap_url =url;
            }
            else if(mode.equals("Other")){
                botInsert(name, chat_text, "_content");
                btnGrap.setVisibility(View.INVISIBLE);
            }
            else{
                botInsert(name, "Mode 이상함", "_content");
            }





        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}
