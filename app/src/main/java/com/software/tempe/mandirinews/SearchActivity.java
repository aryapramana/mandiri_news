package com.software.tempe.mandirinews;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.software.tempe.mandirinews.adapter.DataViewAdapter;
import com.software.tempe.mandirinews.application.MainApplication;
import com.software.tempe.mandirinews.constant.Constant;
import com.software.tempe.mandirinews.model.Article;
import com.software.tempe.mandirinews.model.News;
import com.software.tempe.mandirinews.network.OfflineResponseInterceptor;
import com.software.tempe.mandirinews.network.ResponseInterceptor;
import com.software.tempe.mandirinews.service.ServiceClient;
import com.software.tempe.mandirinews.service.ServiceInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    private EditText search_textEditTxt;
    private SwipeRefreshLayout swipe_refresh_search;
    private TextView searchResultTxtView;
    private DataViewAdapter dataViewAdapter;
    private Typeface airbnb_book;
    private RecyclerView searchRecyclerView;
    private List<Article> articleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        AssetManager assetManager = this.getApplicationContext().getAssets();
        airbnb_book = Typeface.createFromAsset(assetManager, "font/airbnbcereal_book.ttf");

        createToolbar();

        search_textEditTxt = findViewById(R.id.search_textEditTxt);
        search_textEditTxt.setTypeface(airbnb_book);
        swipe_refresh_search = findViewById(R.id.swipe_refresh_search);
        searchRecyclerView = findViewById(R.id.searchRecyclerView);
        searchResultTxtView = findViewById(R.id.searchResultTxtView);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        // initiate input
        search_textEditTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    searchQuery(search_textEditTxt.getText().toString().trim());
                    return true;
                }

                return false;
            }
        });

        swipe_refresh_search.setEnabled(false);
        swipe_refresh_search.setColorSchemeResources(R.color.colorAccent);

    }

    private void searchQuery(final String text) {
        swipe_refresh_search.setEnabled(true);
        swipe_refresh_search.setRefreshing(true);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(new ResponseInterceptor());
        httpClient.addInterceptor(new OfflineResponseInterceptor());
        // httpClient.cache(new Cache(new File(MainApplication.getMainApplicationInstance()
           //     .getCacheDir(), "ResponsesCache"), 10 * 1024 * 1024));
        httpClient.readTimeout(10, TimeUnit.SECONDS);
        httpClient.connectTimeout(10, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);

        ServiceInterface request = ServiceClient.getClient(httpClient).create(ServiceInterface.class);

        String sortBy = "publishedAt";
        String language = "en";
        Call<News> call = request.getSearchResults(text, sortBy, language, Constant.API_KEY);
        call.enqueue(new Callback<News>() {

            @Override
            public void onResponse(@NonNull Call<News> call, @NonNull Response<News> response) {

                if (response.isSuccessful() && response.body().getArticles() != null) {

                    if (response.body().getTotalResults() != 0) {
                        if (!articleList.isEmpty()) {
                            articleList.clear();
                        }

                        articleList = response.body().getArticles();
                        dataViewAdapter = new DataViewAdapter(articleList, SearchActivity.this);
                        searchRecyclerView.setVisibility(View.VISIBLE);
                        searchResultTxtView.setVisibility(View.GONE);
                        searchRecyclerView.setAdapter(dataViewAdapter);
                        swipe_refresh_search.setRefreshing(false);
                        swipe_refresh_search.setEnabled(false);
                    } else if (response.body().getTotalResults() == 0){
                        swipe_refresh_search.setRefreshing(false);
                        swipe_refresh_search.setEnabled(false);
                        searchResultTxtView.setVisibility(View.VISIBLE);
                        searchRecyclerView.setVisibility(View.GONE);
                        searchResultTxtView.setText("No Results: \"" + text + "\"." );
                    }
                }
            }


            @Override
            public void onFailure(@NonNull Call<News> call, @NonNull Throwable t) {
                swipe_refresh_search.setRefreshing(false);
                swipe_refresh_search.setEnabled(false);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void createToolbar() {
        Toolbar search_toolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(search_toolbar);
        if (getSupportActionBar() != null)  {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        search_toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_dark_foreground));
        search_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_cancel) {
            search_textEditTxt.setText("");
            search_textEditTxt.requestFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            mgr.showSoftInput(search_textEditTxt, InputMethodManager.SHOW_IMPLICIT);
            searchRecyclerView.setVisibility(View.GONE);
        }
        return super.onOptionsItemSelected(item);
    }
}
