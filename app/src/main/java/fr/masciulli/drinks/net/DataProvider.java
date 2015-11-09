package fr.masciulli.drinks.net;

import android.content.Context;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import retrofit.Call;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

public class DataProvider {

    private static final String CACHE_RESPONSES_DIR = "responses";
    private static final String SERVER_BASE_URL = "http://drinks-api.appspot.com";
    private static final long CACHE_MAX_SIZE = 10 * 1024 * 1024;

    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_CACHE_CONTROL_PUBLIC = "public";
    private static final String HEADER_CACHE_CONTROL_ONLY_IF_CACHED = "public, only-if-cached, max-stale=";
    private static final int HEADER_CACHE_CONTROL_MAX_STATE = 60 * 60 * 24 * 28;

    private WebApi retrofit;

    public DataProvider(final Context context) {

        File httpCacheDirectory = new File(context.getCacheDir(), CACHE_RESPONSES_DIR);
        Cache cache = new Cache(httpCacheDirectory, CACHE_MAX_SIZE);

        OkHttpClient client = new OkHttpClient();
        client.interceptors().add(new Interceptor() {
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
        });
        client.setCache(cache);

        retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebApi.class);
    }

    public Call<List<Drink>> getDrinks() {
        return retrofit.getDrinks();
    }

    public Call<List<Liquor>> getLiquors() {
        return retrofit.getLiquors();
    }
}
