package com.example.go4lunch.ViewModel;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.go4lunch.R;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        WebView mWebView = findViewById(R.id.webview);
        String mURL = getIntent().getStringExtra("DetailRestauUrl");

        mWebView.setWebViewClient(new WebViewClient());

        mWebView.loadUrl(mURL);
    }
}
