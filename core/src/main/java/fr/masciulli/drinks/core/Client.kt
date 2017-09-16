package fr.masciulli.drinks.core

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Observable

class Client(baseUrl: String) : DrinksSource, LiquorsSource {

    private val retrofit: WebApi

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)

        val clientBuilder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)

        retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebApi::class.java)
    }

    override val drinks: Observable<List<Drink>> = retrofit.drinks

    override val liquors: Observable<List<Liquor>> = retrofit.liquors
}
