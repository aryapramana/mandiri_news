package com.software.tempe.mandirinews.network;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        originalResponse.newBuilder()
                .removeHeader("Pragma")
                .header("Cache-Control", "public, max-age=" + 60)
                .build();

        return originalResponse;
    }
}
