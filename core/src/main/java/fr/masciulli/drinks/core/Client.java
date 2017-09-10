package fr.masciulli.drinks.core;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebApi.class);
    }

    @NotNull
    @Override
    public Observable<List<Drink>> getDrinks() {
        return retrofit.getDrinks();
    }

    @Override
    @NotNull
    public Observable<List<Liquor>> getLiquors() {
        return retrofit.getLiquors();
    }
}
