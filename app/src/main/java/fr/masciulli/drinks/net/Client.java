package fr.masciulli.drinks.net;

import android.support.annotation.NonNull;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.List;

import fr.masciulli.drinks.BuildConfig;
import fr.masciulli.drinks.core.Drink;
import fr.masciulli.drinks.core.DrinksSource;
import fr.masciulli.drinks.core.Liquor;
import fr.masciulli.drinks.core.LiquorsSource;
import fr.masciulli.drinks.core.WebApi;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class Client implements DrinksSource, LiquorsSource {
    private static final String SERVER_BASE_URL = "http://drinks-api.appspot.com";

    private WebApi retrofit;

    public Client() {
        this(SERVER_BASE_URL);
    }

    Client(String baseUrl) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor());

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
