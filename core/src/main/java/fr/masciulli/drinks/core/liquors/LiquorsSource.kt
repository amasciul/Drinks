package fr.masciulli.drinks.core.liquors

import io.reactivex.Single

interface LiquorsSource {
    fun getLiquors(): Single<List<Liquor>>
    fun getLiquor(id: String): Single<Liquor>
}

