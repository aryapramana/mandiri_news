package com.software.tempe.mandirinews;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.software.tempe.mandirinews.constant.Constant;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class NewsActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private Button news_detail_button;

    private TextView news_detail_title;
    private TextView news_detail_describe;

    private ImageView news_detailImgView;

    private FloatingActionButton news_detail_float;

    private Typeface airbnbcereal_bold;
    private Typeface airbnbcereal_light;

    private String URL_ADDRESS;
    private String TITLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        createToolbar();

        // set view
        news_detail_button = findViewById(R.id.news_detail_button);

        news_detail_title = findViewById(R.id.news_detail_title);
        news_detail_describe = findViewById(R.id.news_detail_describe);

        news_detailImgView = findViewById(R.id.news_detailImgView);

        news_detail_float = findViewById(R.id.news_detail_float);

        // set font
        AssetManager assetManager = this.getApplicationContext().getAssets();
        airbnbcereal_bold = Typeface.createFromAsset(assetManager, "font/airbnbcereal_bold.ttf");
        airbnbcereal_light = Typeface.createFromAsset(assetManager, "font/airbnbcereal_light.ttf");

        actionShare();

        loadData();

        openNewsWeb();

    }

    private void openNewsWeb() {
        news_detail_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView();
            }
        });
    }

    private void openWebView() {
        Intent browserIntent = new Intent(NewsActivity.this, ViewNewsActivity.class);
        browserIntent.putExtra(Constant.INTENT_HEADLINE, TITLE);
        browserIntent.putExtra(Constant.INTENT_URL, URL_ADDRESS);
        startActivity(browserIntent);
    }

    private void loadData() {
        TITLE = getIntent().getStringExtra(Constant.INTENT_HEADLINE);
        String description = getIntent().getStringExtra(Constant.INTENT_DESCRIPTION);
        URL_ADDRESS = getIntent().getStringExtra(Constant.INTENT_ARTICLE_URL);
        String image_url = getIntent().getStringExtra(Constant.INTENT_IMG_URL);

        news_detail_title.setText(TITLE);
        news_detail_title.setTypeface(airbnbcereal_bold);

        news_detail_describe.setText(description);
        news_detail_describe.setTypeface(airbnbcereal_light);

        try {
            Picasso.get()
                    .load(image_url)
                    .fit()
                    .error(R.mipmap.ic_launcher)
                    .into(news_detailImgView);

        } catch (Exception e)   {
            System.out.println("NewsActivity msg: " + e.getMessage());
        }
    }

    private void actionShare() {
        news_detail_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent actionShareIntent = new Intent();
                actionShareIntent.setAction(Intent.ACTION_SEND);
                actionShareIntent.setType("text/plain");
                actionShareIntent.putExtra(Intent.EXTRA_TEXT, "I just share a news for you! Send from Mandiri News app\n" + Uri.parse(URL_ADDRESS));
                startActivity(Intent.createChooser(actionShareIntent, "Share news via"));
            }
        });
    }

    private void createToolbar() {
        toolbar = findViewById(R.id.news_detail_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)  {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        try {
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        } catch (Exception e)   {
            System.out.println("message: " + e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
