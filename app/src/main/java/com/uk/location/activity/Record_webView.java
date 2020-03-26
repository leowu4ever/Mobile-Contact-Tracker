package com.uk.location.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Record_webView extends Activity {

    WebView webview;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_record);

        webview = (WebView)findViewById(R.id.activity_main_webview);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String url="https://beta.html5test.com/";
        String postData = null;
        try {
            postData = "boo=" + URLEncoder.encode("a", "UTF-8") + "&foo=" + URLEncoder.encode("a", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            postData = "";
        }
        webview.postUrl(url,postData.getBytes());
    }
}
