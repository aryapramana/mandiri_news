package com.software.tempe.mandirinews.service;

import com.software.tempe.mandirinews.constant.Constant;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceClient {

    private static Retrofit retrofit = null;


    public static Retrofit getClient(OkHttpClient.Builder httpClient)   {
        if (retrofit == null)   {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .build();

        }

        return retrofit;
    }

}
