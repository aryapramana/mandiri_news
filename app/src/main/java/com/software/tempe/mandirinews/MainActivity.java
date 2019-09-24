package com.software.tempe.mandirinews;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.software.tempe.mandirinews.adapter.DataViewAdapter;
import com.software.tempe.mandirinews.constant.Constant;
import com.software.tempe.mandirinews.model.Article;
import com.software.tempe.mandirinews.model.News;
import com.software.tempe.mandirinews.network.OfflineResponseInterceptor;
import com.software.tempe.mandirinews.network.ResponseInterceptor;
import com.software.tempe.mandirinews.service.ServiceClient;
import com.software.tempe.mandirinews.service.ServiceInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.cketti.mailto.EmailIntentBuilder;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private String[] NEWS_SOURCE_ARRAY = {"time", "bbc-news", "the-huffington-post", "cnn",
            "entertainment-weekly", "ign", "mtv-news", "bbc-sport", "espn-cric-info",
            "talksport", "medical-news-today", "national-geographic", "engadget", "wired",
            "hacker-news", "the-verge", "techcrunch", "techradar", "fortune",
            "the-wall-street-journal", "business-insider"};

    private String NEWS_SOURCE;

    private List<Article> articleArrayList = new ArrayList<>();

    private DataViewAdapter dataViewAdapter;
    private Drawer drawer;
    private AccountHeader accountHeader;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Parcelable parcelable;
    private Typeface airbnb_book;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager assetManager = this.getApplicationContext().getAssets();
        airbnb_book = Typeface.createFromAsset(assetManager, "font/airbnbcereal_book.ttf");

        createToolbar();

        createRecyclerView();

        NEWS_SOURCE = NEWS_SOURCE_ARRAY[0];
        title.setText(R.string.toolbar_default_text);

        onLoadRefresh();

        createDrawer(savedInstanceState, toolbar, airbnb_book);

    }

    private void createDrawer(Bundle savedInstanceState, Toolbar toolbar, Typeface airbnb_book) {
        SectionDrawerItem item0 = new SectionDrawerItem().withIdentifier(0).withName("GENERAL")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("TIME")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("BBC News")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("The Huffington Post")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("CNN")
                .withTypeface(airbnb_book);
        SectionDrawerItem item5 = new SectionDrawerItem().withIdentifier(5).withName("ENTERTAINMENT")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName("Entertainment Weekly")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item7 = new PrimaryDrawerItem().withIdentifier(7).withName("IGN")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item8 = new PrimaryDrawerItem().withIdentifier(8).withName("MTV News")
                .withTypeface(airbnb_book);
        SectionDrawerItem item9 = new SectionDrawerItem().withIdentifier(9).withName("SPORTS")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item10 = new PrimaryDrawerItem().withIdentifier(10).withName("BBC Sports")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item11 = new PrimaryDrawerItem().withIdentifier(11).withName("ESPN Cric Info")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item12 = new PrimaryDrawerItem().withIdentifier(12).withName("TalkSport")
                .withTypeface(airbnb_book);
        SectionDrawerItem item13 = new SectionDrawerItem().withIdentifier(13).withName("SCIENCE")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item14 = new PrimaryDrawerItem().withIdentifier(14).withName("Medical News Today")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item15 = new PrimaryDrawerItem().withIdentifier(15).withName("National Geographic")
                .withTypeface(airbnb_book);
        SectionDrawerItem item16 = new SectionDrawerItem().withIdentifier(16).withName("TECHNOLOGY")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item17 = new PrimaryDrawerItem().withIdentifier(17).withName("Engadget")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item18 = new PrimaryDrawerItem().withIdentifier(18).withName("Wired")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item19 = new PrimaryDrawerItem().withIdentifier(19).withName("Hacker News")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item20 = new PrimaryDrawerItem().withIdentifier(20).withName("The Verge")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item21 = new PrimaryDrawerItem().withIdentifier(21).withName("TechCrunch")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item22 = new PrimaryDrawerItem().withIdentifier(22).withName("TechRadar")
                .withTypeface(airbnb_book);
        SectionDrawerItem item23 = new SectionDrawerItem().withIdentifier(23).withName("BUSINESS")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item24 = new PrimaryDrawerItem().withIdentifier(24).withName("Fortune")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item25 = new PrimaryDrawerItem().withIdentifier(25).withName("The Wall Street Journal")
                .withTypeface(airbnb_book);
        PrimaryDrawerItem item26 = new PrimaryDrawerItem().withIdentifier(26).withName("Business Insider")
                .withTypeface(airbnb_book);
        SectionDrawerItem item27 = new SectionDrawerItem().withIdentifier(27).withName("ADDITIONAL INFO")
                .withTypeface(airbnb_book);
        SecondaryDrawerItem item28 = new SecondaryDrawerItem().withIdentifier(28).withName("More about this app")
                .withTypeface(airbnb_book);
        SecondaryDrawerItem item29 = new SecondaryDrawerItem().withIdentifier(29).withName("News by newsapi.org")
                .withTypeface(airbnb_book);
        SecondaryDrawerItem item30 = new SecondaryDrawerItem().withIdentifier(30).withName("Send feedback to us")
                .withTypeface(airbnb_book);

        accountHeader = new AccountHeaderBuilder()
                .withHeaderBackground(R.drawable.mandiri_account_header)
                .withActivity(this)
                .withSavedInstance(savedInstanceState)
                .build();

        drawer = new DrawerBuilder()
                .withAccountHeader(accountHeader)
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(1)
                .addDrawerItems(item0, item1, item2, item3, item4, item5, item6, item7, item8, item9,
                        item10, item11, item12, item13, item14, item15, item16, item17, item18, item19,
                        item20, item21, item22, item23, item24, item25, item26, item27, item28, item29, item30)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int selected = (int) (long) drawerItem.getIdentifier();
                        switch (selected) {
                            case 1:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[0];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 2:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[1];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 3:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[2];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 4:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[3];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 6:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[4];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 7:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[5];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 8:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[6];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 10:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[7];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 11:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[8];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 12:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[9];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 14:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[10];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 15:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[11];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 17:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[12];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 18:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[13];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 19:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[14];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 20:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[15];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 21:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[16];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 22:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[17];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 24:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[18];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 25:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[19];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 26:
                                NEWS_SOURCE = NEWS_SOURCE_ARRAY[20];
                                onLoadRefresh();
                                title.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 28:
                                goAboutActivity();
                                break;
                            case 29:
                                Intent browserAPI = new Intent(Intent.ACTION_VIEW, Uri.parse("https://newsapi.org/"));
                                startActivity(browserAPI);
                                break;
                            case 30:
                                sendMail();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    private void onLoadRefresh() {
//        if (!NetworkStats.isNetworkAvailble()) {
//            Toast.makeText(MainActivity.this, "Please check your internet connection!", Toast.LENGTH_LONG).show();
//        }

        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                }
        );
    }

    private void createRecyclerView() {
        recyclerView = findViewById(R.id.newsRecyclerView);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    private void createToolbar() {
        toolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)  {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        title = findViewById(R.id.toolbar_title);
        title.setTypeface(airbnb_book);
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(new ResponseInterceptor());
        httpClient.addInterceptor(new OfflineResponseInterceptor());
        httpClient.readTimeout(10, TimeUnit.SECONDS);
        httpClient.connectTimeout(10, TimeUnit.SECONDS);
        httpClient.addInterceptor(interceptor);

        ServiceInterface serviceInterface = ServiceClient.getClient(httpClient).create(ServiceInterface.class);

        Call<News> newsCall = serviceInterface.getHeadlines(NEWS_SOURCE, Constant.API_KEY);

        newsCall.enqueue(new Callback<News>() {
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (!response.isSuccessful())   {
                    Toast.makeText(MainActivity.this, "message: " + response.code(), Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                }

                if (response.body() != null)    {
                    if (!articleArrayList.isEmpty())    {
                        articleArrayList.clear();
                    }

                    articleArrayList = response.body().getArticles();

                    dataViewAdapter = new DataViewAdapter(articleArrayList, MainActivity.this);
                    recyclerView.setAdapter(dataViewAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {
                Toast.makeText(MainActivity.this, "message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
                onStart();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_menu:
                goAboutActivity();
                break;
            case R.id.action_search:
                goSearchActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goSearchActivity() {
        Intent intent = new Intent(MainActivity.this, SearchActivity.class);
        startActivity(intent);
    }

    private void goAboutActivity() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    private void sendMail() {
        Intent emailIntent = EmailIntentBuilder.from(this)
                .to("tempe.software@gmail.com")
                .subject("Mandiri News Feedback")
                .build();
        startActivity(Intent.createChooser(emailIntent, "Mandiri News Feedback"));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen())  {
            drawer.closeDrawer();
        } else {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this, R.style.CustomAlertDialogStyle);
            alertBuilder.setTitle("Mandiri News");
            alertBuilder.setMessage("Do you want to exit the application?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (parcelable != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(parcelable);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            NEWS_SOURCE = savedInstanceState.getString(Constant.SOURCE);
            createToolbar();

            title.setText(savedInstanceState.getString(Constant.TITLE_STATE));

            parcelable = savedInstanceState.getParcelable(Constant.RECYCLER_STATE);
            createDrawer(savedInstanceState, toolbar, airbnb_book);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = drawer.saveInstanceState(outState);
        outState = accountHeader.saveInstanceState(outState);

        super.onSaveInstanceState(outState);
        try {
            parcelable = recyclerView.getLayoutManager().onSaveInstanceState();
        } catch (Exception e)   {
            System.out.println(e.getMessage());
        }
        outState.putParcelable(Constant.RECYCLER_STATE, parcelable);
        outState.putString(Constant.SOURCE, NEWS_SOURCE);
        outState.putString(Constant.TITLE_STATE, title.getText().toString());

    }
}
