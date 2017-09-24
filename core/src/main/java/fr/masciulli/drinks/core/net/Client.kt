package fr.masciulli.drinks.core.net

import fr.masciulli.drinks.core.drinks.Drink
import fr.masciulli.drinks.core.drinks.DrinksSource
import fr.masciulli.drinks.core.liquors.Liquor
import fr.masciulli.drinks.core.liquors.LiquorsSource
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

    override fun getDrinks() = retrofit.drinks

    //TODO use dedicated endpoint
    override fun getDrink(id: String): Observable<Drink> = getDrinks()
            .flatMap({ Observable.from(it) })
            .filter({ it.id == id })

    override val liquors: Observable<List<Liquor>> = retrofit.liquors
}
