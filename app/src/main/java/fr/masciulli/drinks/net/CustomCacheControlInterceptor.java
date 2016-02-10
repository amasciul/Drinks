package fr.masciulli.drinks.net;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CustomCacheControlInterceptor implements Interceptor {
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_CACHE_CONTROL_PUBLIC = "public";
    private static final String HEADER_CACHE_CONTROL_ONLY_IF_CACHED = "public, only-if-cached, max-stale=";
    private static final int HEADER_CACHE_CONTROL_MAX_STATE = 60 * 60 * 24 * 28;

    private final Context context;

    public CustomCacheControlInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        if (new ConnectivityChecker(context).isConnectedOrConnecting()) {
            builder.addHeader(HEADER_CACHE_CONTROL, HEADER_CACHE_CONTROL_PUBLIC);
        } else {
            builder.addHeader(HEADER_CACHE_CONTROL,
                    HEADER_CACHE_CONTROL_ONLY_IF_CACHED + HEADER_CACHE_CONTROL_MAX_STATE);
        }
        return chain.proceed(builder.build());
    }
}
