package com.example.user.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.Hashtable;

import static  com.example.user.chatbot.R.id.webView;

public class MenuActivity extends AppCompatActivity {
    private WebView mWebView;
    private String myUrl = "http://iter7.jbnu.ac.kr/daum_stock9.html"; // 접속 URL (내장HTML의 경우 왼쪽과 같이 쓰고 아니면 걍 URL)
    Button chat_btn;
    Button rec_com_btn;
    Button HowBtn;
    Button rec_els_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        // 웹뷰 셋팅팅
        mWebView = (WebView) findViewById(webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        //mWebView.loadUrl("http://www.pois.co.kr/mobile/login.do");

        mWebView.loadUrl(myUrl); // 접속 URL
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClientClass());

        //find btn
        chat_btn = (Button)findViewById(R.id.menu_chat_btn);
        rec_com_btn = (Button)findViewById(R.id.rec_com);
        HowBtn = (Button)findViewById(R.id.HowBtn);
        rec_els_btn = (Button)findViewById(R.id.rec_els);

        chat_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent in = new Intent(MenuActivity.this , ChatActivity.class);
                startActivity(in);
            }
        });

        rec_com_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent in = new Intent(MenuActivity.this , recommend_company_Activity.class);
                startActivity(in);
            }
        });

        HowBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent in = new Intent(MenuActivity.this , HowActivity.class);
                startActivity(in);
            }
        });
        rec_els_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent in = new Intent(MenuActivity.this , recommend_ELS_Activity.class);
                startActivity(in);
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL",url);
            view.loadUrl(url);
            return true;
        }
    }


}
