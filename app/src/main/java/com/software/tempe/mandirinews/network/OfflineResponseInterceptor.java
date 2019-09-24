package com.software.tempe.mandirinews.network;

import com.software.tempe.mandirinews.utility.NetworkStats;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class OfflineResponseInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
//        if (!NetworkStats.isNetworkAvailble())  {
//            request = request.newBuilder()
//                    .removeHeader("Pragma")
//                    .header("Cache-Control", "public, only -if-cached, max-stale=" + 2419200)
//                    .build();
//        }

        return chain.proceed(request);
    }
}
