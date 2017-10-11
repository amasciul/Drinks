package fr.masciulli.drinks.core.net

import fr.masciulli.drinks.core.drinks.Drink
import fr.masciulli.drinks.core.liquors.Liquor
import io.reactivex.Single
import retrofit2.http.GET

interface WebApi {
    @get:GET("/api/Drinks")
    val drinks: Single<List<Drink>>

    @get:GET("/api/Liquors")
    val liquors: Single<List<Liquor>>
}
