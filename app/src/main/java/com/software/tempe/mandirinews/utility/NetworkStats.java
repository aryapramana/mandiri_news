package com.software.tempe.mandirinews.utility;

import android.content.Context;
import android.net.ConnectivityManager;

import com.software.tempe.mandirinews.application.MainApplication;

public class NetworkStats {

    public static boolean isNetworkAvailble()   {
        ConnectivityManager connectivityManager = (ConnectivityManager) MainApplication.getMainApplicationInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }

}
