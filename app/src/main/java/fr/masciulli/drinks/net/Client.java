package fr.masciulli.drinks.net;

import android.content.Context;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.util.List;

import fr.masciulli.drinks.BuildConfig;
import fr.masciulli.drinks.model.Drink;
import fr.masciulli.drinks.model.Liquor;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class Client {

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

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebApi.class);
    }

    public Observable<List<Drink>> getDrinks() {
        return retrofit.getDrinks();
    }

    public Observable<List<Liquor>> getLiquors() {
        return retrofit.getLiquors();
    }
}
