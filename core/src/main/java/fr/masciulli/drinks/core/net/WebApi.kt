package fr.masciulli.drinks.core.net

import fr.masciulli.drinks.core.liquors.Liquor
import fr.masciulli.drinks.core.drinks.Drink
import retrofit2.http.GET
import rx.Observable

interface WebApi {
    @get:GET("/api/Drinks")
    val drinks: Observable<List<Drink>>

    @get:GET("/api/Liquors")
    val liquors: Observable<List<Liquor>>
}
