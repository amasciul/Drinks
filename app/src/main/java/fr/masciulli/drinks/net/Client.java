package fr.masciulli.drinks.net;

import android.content.Context;
import android.support.annotation.NonNull;

import auto.parcelgson.gson.AutoParcelGsonTypeAdapterFactory;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.List;

import fr.masciulli.drinks.BuildConfig;
import fr.masciulli.drinks.core.Drink;
import fr.masciulli.drinks.core.DrinksSource;
import fr.masciulli.drinks.core.Liquor;
import fr.masciulli.drinks.core.LiquorsSource;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class Client implements DrinksSource, LiquorsSource {

    private static final String CACHE_RESPONSES_DIR = "responses";
    private static final String SERVER_BASE_URL = "http://drinks-api.appspot.com";
    private static final long CACHE_MAX_SIZE = 10 * 1024 * 1024;

    private WebApi retrofit;

    public Client(Context context) {
        this(context, SERVER_BASE_URL);
    }

    Client(Context context, String baseUrl) {
        File httpCacheDirectory = new File(context.getCacheDir(), CACHE_RESPONSES_DIR);
        Cache cache = new Cache(httpCacheDirectory, CACHE_MAX_SIZE);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .cache(cache)
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(new CustomCacheControlInterceptor(context));

        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
        }

        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new AutoParcelGsonTypeAdapterFactory())
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(WebApi.class);
    }

    @Override
    @NonNull
    public Observable<List<Drink>> getDrinks() {
        return retrofit.getDrinks();
    }

    @Override
    @NonNull
    public Observable<List<Liquor>> getLiquors() {
        return retrofit.getLiquors();
    }
}
