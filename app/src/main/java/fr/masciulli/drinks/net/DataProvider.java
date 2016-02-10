package fr.masciulli.drinks.net;

import android.content.Context;

import java.io.File;
import java.util.List;

import fr.masciulli.drinks.BuildConfig;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataProvider {

    private static final String CACHE_RESPONSES_DIR = "responses";
    private static final String SERVER_BASE_URL = "http://drinks-api.appspot.com";
    private static final long CACHE_MAX_SIZE = 10 * 1024 * 1024;

    private WebApi retrofit;

    public DataProvider(final Context context) {

        File httpCacheDirectory = new File(context.getCacheDir(), CACHE_RESPONSES_DIR);
        Cache cache = new Cache(httpCacheDirectory, CACHE_MAX_SIZE);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(new CustomCacheControlInterceptor(context));

        if (BuildConfig.DEBUG) {
            clientBuilder.addInterceptor(loggingInterceptor);
        }

        retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_BASE_URL)
                .client(clientBuilder.build())
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
