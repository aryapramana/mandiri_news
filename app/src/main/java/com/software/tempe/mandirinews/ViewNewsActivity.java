package com.software.tempe.mandirinews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.http.SslError;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.software.tempe.mandirinews.constant.Constant;

import java.util.Objects;

public class ViewNewsActivity extends AppCompatActivity {

    private ProgressBar news_view_progress;

    private TextView news_view_title;

    private Toolbar news_view_toolbar;

    private Typeface airbnbcereal_book;

    private String news_url;
    private String news_title;

    private WebView news_view_web;

    private float position_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);

        AssetManager assetManager = this.getApplicationContext().getAssets();
        airbnbcereal_book = Typeface.createFromAsset(assetManager, "font/airbnbcereal_book.ttf");

        news_url = getIntent().getStringExtra(Constant.INTENT_URL);
        news_title = getIntent().getStringExtra(Constant.INTENT_HEADLINE);

        // set view
        news_view_progress = findViewById(R.id.news_view_progress);

        news_view_title = findViewById(R.id.news_view_title);

        news_view_web = findViewById(R.id.news_view_web);

        createToolbar();

        if (savedInstanceState == null) {
            news_view_web.loadUrl(news_url);
            loadWeb();
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private void loadWeb() {
        news_view_web.setWebChromeClient(new CustomWebChromeClient(this));
        news_view_web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                news_view_progress.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                news_view_web.loadUrl(url);
                news_view_progress.setVisibility(View.GONE);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                news_view_progress.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

        });

        news_view_web.clearCache(true);
        news_view_web.clearHistory();
        news_view_web.getSettings().setJavaScriptEnabled(true);
        news_view_web.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        news_view_web.getSettings().setDomStorageEnabled(true);
        news_view_web.setHorizontalScrollBarEnabled(false);

        news_view_web.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        position_x = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(position_x, event.getY());
                    }
                    break;
                }

                return false;
            }


        });

    }

    private void createToolbar() {
        news_view_toolbar = findViewById(R.id.news_view_toolbar);
        setSupportActionBar(news_view_toolbar);
        if (getSupportActionBar() != null)  {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } catch (Exception e)   {
            System.out.println("message: " + e.getMessage());
        }
        getSupportActionBar().setSubtitle(news_url);

        news_view_title.setTypeface(airbnbcereal_book);
        news_view_title.setText(news_title);

    }

    private class CustomWebChromeClient extends WebChromeClient {
        Context context;

        public CustomWebChromeClient(Context context) {
            super();
            this.context = context;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (news_view_web.canGoBack()) {
                    news_view_web.goBack();
                } else {
                    finish();
                }
                return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }
}
