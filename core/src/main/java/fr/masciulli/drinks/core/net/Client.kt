package fr.masciulli.drinks.core.net

import fr.masciulli.drinks.core.drinks.Drink
import fr.masciulli.drinks.core.drinks.DrinksSource
import fr.masciulli.drinks.core.liquors.Liquor
import fr.masciulli.drinks.core.liquors.LiquorsSource
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WebApi::class.java)
    }

    override fun getDrinks() = retrofit.drinks

    //TODO use dedicated endpoint
    override fun getDrink(id: String): Single<Drink> = getDrinks()
            .toObservable()
            .flatMap { Observable.fromIterable(it) }
            .filter { it.id == id }
            .singleOrError()

    override fun getLiquors() = retrofit.liquors

    //TODO use dedicated endpoint
    override fun getLiquor(id: String): Single<Liquor> {
        return getLiquors()
                .toObservable()
                .flatMap { Observable.fromIterable(it) }
                .filter { it.id == id }
                .singleOrError()
    }
}
