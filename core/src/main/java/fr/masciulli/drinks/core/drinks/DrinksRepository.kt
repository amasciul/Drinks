package fr.masciulli.drinks.core.drinks

import rx.Observable

class DrinksRepository(private val remoteSource: DrinksSource) : DrinksSource {
    override val drinks: Observable<List<Drink>>
        get() = remoteSource.drinks
}