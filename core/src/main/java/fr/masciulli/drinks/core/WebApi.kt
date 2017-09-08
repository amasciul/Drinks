package fr.masciulli.drinks.core

import retrofit2.http.GET
import rx.Observable

interface WebApi {
    @get:GET("/api/Drinks")
    val drinks: Observable<List<Drink>>

    @get:GET("/api/Liquors")
    val liquors: Observable<List<Liquor>>
}
