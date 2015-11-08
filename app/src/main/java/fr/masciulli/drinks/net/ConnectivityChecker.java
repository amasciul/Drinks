package fr.masciulli.drinks.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityChecker {
    private final ConnectivityManager connectivityManager;

    public ConnectivityChecker(Context context) {
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }


    public boolean isConnectedOrConnecting() {
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
