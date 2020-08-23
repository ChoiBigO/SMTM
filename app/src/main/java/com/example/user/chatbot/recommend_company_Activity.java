package com.example.user.chatbot;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.SyncStateContract.Constants;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;


public class recommend_company_Activity extends AppCompatActivity {

    private static String IP_ADDRESS = "IP주소";
    private static String TAG = "phptest";

    private EditText mEditTextName;
    private EditText mEditTextCountry;
    private TextView mTextViewResult1,mTextViewResult2,mTextViewResult3;

    private EditText mEditTextSearchKeyword;
    private String mJsonString;
    private ImageView imgView1,imgView2,imgView3;
    ProgressDialog progressDialog;
    Bitmap bm1 , bm2,bm3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_company_);

        mTextViewResult1 = (TextView)findViewById(R.id.rec_com_result1);
        mTextViewResult1.setMovementMethod(new ScrollingMovementMethod());
        mTextViewResult2 = (TextView)findViewById(R.id.rec_com_result2);
        mTextViewResult2.setMovementMethod(new ScrollingMovementMethod());
        mTextViewResult3 = (TextView)findViewById(R.id.rec_com_result3);
        mTextViewResult3.setMovementMethod(new ScrollingMovementMethod());

        GetData task = new GetData();
        task.execute( "http://iter7.jbnu.ac.kr/daewon/get_recommend_com.php", "");

        imgView1 = (ImageView)findViewById(R.id.imgView1);
        imgView2 = (ImageView)findViewById(R.id.imgView2);
        imgView3 = (ImageView)findViewById(R.id.imgView3);

        Toast.makeText(recommend_company_Activity.this, "그래프 터치시 확대", Toast.LENGTH_LONG).show();
    }


    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(recommend_company_Activity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            mTextViewResult1.setText(result);
            mTextViewResult2.setText(result);
            mTextViewResult3.setText(result);

            Log.d(TAG, "response - " + result);

            if (result == null){

                mTextViewResult1.setText(errorString);
                mTextViewResult2.setText(errorString);
                mTextViewResult3.setText(errorString);
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


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
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
        String TAG_COMPANY1 = "company1";
        String TAG_ABS1 = "absolute_value1";
        String TAG_RAT1 = "ratio_value1";
        String TAG_COMPANY2 = "company2";
        String TAG_ABS2 = "absolute_value2";
        String TAG_RAT2 = "ratio_value2";
        String TAG_COMPANY3 = "company3";
        String TAG_ABS3 = "absolute_value3";
        String TAG_RAT3 = "ratio_value3";

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray(TAG_JSON);


            JSONObject item = jsonArray.getJSONObject(0);

            final String company1 = item.getString(TAG_COMPANY1);
            String absolute1 = item.getString(TAG_ABS1);
            String ratio1 = item.getString(TAG_RAT1);
            final String company2 = item.getString(TAG_COMPANY2);
            String absolute2 = item.getString(TAG_ABS2);
            String ratio2 = item.getString(TAG_RAT2);
            final String company3 = item.getString(TAG_COMPANY3);
            String absolute3 = item.getString(TAG_ABS3);
            String ratio3 = item.getString(TAG_RAT3);

            mTextViewResult1.setText("회사명 : "+company1+"\n\n예상 가격 : "+absolute1+"\n\n예상 비율 : "+ratio1 );
            mTextViewResult2.setText("회사명 : "+company2+"\n\n예상 가격 : "+absolute2+"\n\n예상 비율 : "+ratio2 );
            mTextViewResult3.setText("회사명 : "+company3+"\n\n예상 가격 : "+absolute3+"\n\n예상 비율 : "+ratio3 );



            Thread mThread = new Thread(){

                @Override
                public void run(){
                    try {
                        URL url1 = new URL("http://iter7.jbnu.ac.kr/result_png/"+company1+".png");
                        HttpURLConnection conn1 = (HttpURLConnection)url1.openConnection();
                        conn1.setDoInput(true);
                        conn1.connect();
                        InputStream is1 = conn1.getInputStream();
                        bm1 = BitmapFactory.decodeStream(is1);

                        URL url2 = new URL("http://iter7.jbnu.ac.kr/result_png/"+company2+".png");
                        HttpURLConnection conn2 = (HttpURLConnection)url2.openConnection();
                        conn2.setDoInput(true);
                        conn2.connect();
                        InputStream is2 = conn2.getInputStream();
                        bm2 = BitmapFactory.decodeStream(is2);

                        URL url3 = new URL("http://iter7.jbnu.ac.kr/result_png/"+company3+".png");
                        HttpURLConnection conn3 = (HttpURLConnection)url3.openConnection();
                        conn3.setDoInput(true);
                        conn3.connect();
                        InputStream is3 = conn3.getInputStream();
                        bm3 = BitmapFactory.decodeStream(is3);

                    }catch (IOException ex){

                    }
                }

            };

            mThread.start();

            try {
                mThread.join();
                imgView1.setImageBitmap(bm1);
                imgView2.setImageBitmap(bm2);
                imgView3.setImageBitmap(bm3);

            }catch (InterruptedException e){

            }

            imgView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://iter7.jbnu.ac.kr/result_png/getpng.php?id="+company1));
                    startActivity(intent);

                }
            });
            imgView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://iter7.jbnu.ac.kr/result_png/getpng.php?id="+company2));
                    startActivity(intent);

                }
            });
            imgView3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://iter7.jbnu.ac.kr/result_png/getpng.php?id="+company3));
                    startActivity(intent);

                }
            });



        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }

}