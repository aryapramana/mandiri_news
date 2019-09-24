package com.software.tempe.mandirinews.service;

import com.software.tempe.mandirinews.model.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceInterface {

    @GET("everything")
    Call<News> getSearchResults(@Query("q") String query,
                                @Query("sortBy") String sortBy,
                                @Query("language") String language,
                                @Query("apiKey") String apiKey);


    @GET("top-headlines")
    Call<News> getHeadlines(@Query("sources") String sources,
                                    @Query("apiKey") String apiKey);

}
