package fr.masciulli.drinks.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * A helper class used to determine the current network state, i.e. online or not online
 * Shamefully stolen from http://github.com/tkeunebr
 */
public final class ConnectionUtils {

    public static boolean isOnline(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}